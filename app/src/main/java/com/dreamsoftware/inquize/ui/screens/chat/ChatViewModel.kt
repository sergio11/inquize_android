package com.dreamsoftware.inquize.ui.screens.chat

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.inquize.data.local.bitmapstore.BitmapStore
import com.dreamsoftware.inquize.data.local.preferences.UserPreferencesManager
import com.dreamsoftware.inquize.data.remote.datasource.IMultiModalLanguageModelDataSource
import com.dreamsoftware.inquize.data.remote.dto.QuestionWithImageDTO
import com.dreamsoftware.inquize.domain.model.ChatMessageBO
import com.dreamsoftware.inquize.domain.service.ITranscriptionService
import com.dreamsoftware.inquize.domain.service.ITTSService
import com.dreamsoftware.inquize.ui.navigation.PerceiveNavigationDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Base64
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val ITranscriptionService: ITranscriptionService,
    private val ITTSService: ITTSService,
    private val languageModelClient: IMultiModalLanguageModelDataSource,
    private val bitmapStore: BitmapStore,
    private val preferencesManager: UserPreferencesManager,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val initialUserChatMessageBO = ChatMessageBO(
        message = savedStateHandle.get<String>(PerceiveNavigationDestinations.ChatScreen.NAV_ARG_INITIAL_USER_PROMPT)!!,
        role = ChatMessageBO.Role.USER
    )
    private val conversationImageBitmapUri = savedStateHandle
        .get<String>(PerceiveNavigationDestinations.ChatScreen.NAV_ARG_ASSOCIATED_IMAGE_URI_BASE64)!!
        .run {
            Base64.getDecoder().decode(this)
                .let(::String)
                .let(Uri::parse)
        }

    private val _uiState = MutableStateFlow(ChatScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _userSpeechTranscriptionStream = MutableStateFlow<String?>(null)
    val userSpeechTranscriptionStream = _userSpeechTranscriptionStream.asStateFlow()

    init {
        preferencesManager.preferencesStream
            .onEach { perceiveAppPreferences ->
                _uiState.update {
                    it.copy(isAssistantMuted = perceiveAppPreferences.isAssistantMuted)
                }
            }.launchIn(viewModelScope)

        languageModelClient.startNewChatSession()

        // Generate response for initial prompt adding message to chat items list
        _uiState.update { it.copy(messages = listOf(initialUserChatMessageBO)) }
        viewModelScope.launch {
            val initialBitmap = bitmapStore
                .retrieveBitmapForUri(conversationImageBitmapUri) // todo: error handling / delete image after using it
            generateLanguageModelResponseUpdatingUiState(
                messageToModel = initialUserChatMessageBO.message,
                image = initialBitmap
            )
        }
    }

    fun startTranscription() {
        _userSpeechTranscriptionStream.update { "" }
        _uiState.update { it.copy(isListening = true) }
        ITranscriptionService.startListening(
            transcription = { transcription -> _userSpeechTranscriptionStream.update { transcription } },
            onEndOfSpeech = {
                _uiState.update { it.copy(isListening = false) }
                // Checks for checking if transcription is valid
                val userTranscription =
                    _userSpeechTranscriptionStream.value ?: return@startListening
                if (userTranscription.isBlank()) return@startListening

                // If it is a valid transcription, add transcription to chat messages
                // Before adding the transcription to the chat, clear the transcription stream
                _userSpeechTranscriptionStream.update { null }
                val userTranscriptionChatMessageBO = ChatMessageBO(
                    message = userTranscription,
                    role = ChatMessageBO.Role.USER
                )
                _uiState.update { it.copy(messages = it.messages + userTranscriptionChatMessageBO) }

                viewModelScope.launch {
                    generateLanguageModelResponseUpdatingUiState(messageToModel = userTranscription)
                }
            },
            onError = { _uiState.update { it.copy(isListening = false, hasErrorOccurred = true) } }
        )
    }

    fun stopTranscription() {
        _userSpeechTranscriptionStream.update { null }
        ITranscriptionService.stopListening()
        _uiState.update { it.copy(isListening = false) }
    }


    fun onAssistantMutedStateChange(isMuted: Boolean) {
        _uiState.update { it.copy(isAssistantMuted = isMuted) }
        viewModelScope.launch { preferencesManager.setAssistantMutedStatus(isMuted) }
        if (isMuted) ITTSService.stop()
    }

    fun stopAssistantIfSpeaking() {
        if (!_uiState.value.isAssistantSpeaking) return
        ITTSService.stop()
    }

    private suspend fun generateLanguageModelResponseUpdatingUiState(
        messageToModel: String,
        image: Bitmap? = null
    ) {

        val data = QuestionWithImageDTO(
            question = messageToModel,
            image = image!!
        )

        _uiState.update { it.copy(isLoading = true) }
        val modelResponse = languageModelClient.sendMessage(data)
            .getOrNull()
            ?.let { ChatMessageBO(message = it, role = ChatMessageBO.Role.ASSISTANT) }
        if (modelResponse == null) {
            _uiState.update { it.copy(isLoading = false, hasErrorOccurred = true) }
            return
        }
        _uiState.update { it.copy(messages = it.messages + modelResponse) }
        _uiState.update { it.copy(isLoading = false) }
        // speak if not muted
        if (_uiState.value.isAssistantMuted) return
        speakTextUpdatingUiState(text = modelResponse.message)
    }

    private suspend fun speakTextUpdatingUiState(text: String) {
        try {
            _uiState.update { it.copy(isAssistantSpeaking = true) }
            ITTSService.startSpeaking(text = text)
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            _uiState.update { it.copy(hasErrorOccurred = true) }
        } finally {
            _uiState.update { it.copy(isAssistantSpeaking = false) }
        }
    }

    fun clearCache() {
        // Note: Calling this in onCleared is not possible because
        // viewModelScope gets cancelled before onCleared is called.
        viewModelScope.launch { bitmapStore.deleteAllSavedBitmaps() }
    }

    override fun onCleared() {
        languageModelClient.endChatSession()
        ITTSService.stop()
        ITTSService.releaseResources()
        super.onCleared()
    }
}

