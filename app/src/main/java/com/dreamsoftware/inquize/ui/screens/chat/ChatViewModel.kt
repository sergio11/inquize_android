package com.dreamsoftware.inquize.ui.screens.chat

import com.dreamsoftware.brownie.core.BrownieViewModel
import com.dreamsoftware.brownie.core.IBrownieErrorMapper
import com.dreamsoftware.brownie.core.SideEffect
import com.dreamsoftware.brownie.core.UiState
import com.dreamsoftware.brownie.utils.EMPTY
import com.dreamsoftware.inquize.di.ChatErrorMapper
import com.dreamsoftware.inquize.domain.model.InquizeBO
import com.dreamsoftware.inquize.domain.model.InquizeMessageBO
import com.dreamsoftware.inquize.domain.usecase.AddInquizeMessageUseCase
import com.dreamsoftware.inquize.domain.usecase.EndUserSpeechCaptureUseCase
import com.dreamsoftware.inquize.domain.usecase.GetInquizeByIdUseCase
import com.dreamsoftware.inquize.domain.usecase.StopTextToSpeechUseCase
import com.dreamsoftware.inquize.domain.usecase.TextToSpeechUseCase
import com.dreamsoftware.inquize.domain.usecase.TranscribeUserQuestionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*@HiltViewModel
class ChatViewModel @Inject constructor(
    private val ITranscriptionService: ITranscriptionService,
    private val ITTSService: ITTSService,
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

        /*val data = ResolveQuestionDTO(
            question = messageToModel,
            image = image!!
        )

        _uiState.update { it.copy(isLoading = true) }
        val modelResponse = languageModelClient.resolveQuestion(data)
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
        speakTextUpdatingUiState(text = modelResponse.message)*/
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
        //languageModelClient.endChatSession()
        ITTSService.stop()
        ITTSService.releaseResources()
        super.onCleared()
    }
}*/

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getInquizeByIdUseCase: GetInquizeByIdUseCase,
    private val transcribeUserQuestionUseCase: TranscribeUserQuestionUseCase,
    private val endUserSpeechCaptureUseCase: EndUserSpeechCaptureUseCase,
    private val textToSpeechUseCase: TextToSpeechUseCase,
    private val stopTextToSpeechUseCase: StopTextToSpeechUseCase,
    private val addInquizeMessageUseCase: AddInquizeMessageUseCase,
    @ChatErrorMapper private val errorMapper: IBrownieErrorMapper
) : BrownieViewModel<ChatUiState, ChatSideEffects>(), ChatScreenActionListener {

    fun load(id: String) {
        executeUseCaseWithParams(
            useCase = getInquizeByIdUseCase,
            params = GetInquizeByIdUseCase.Params(id = id),
            onSuccess = ::onGetInquizeCompletedSuccessfully,
            onMapExceptionToState = ::onMapExceptionToState
        )
    }

    override fun onGetDefaultState(): ChatUiState = ChatUiState()

    override fun onAssistantMutedChange(isMuted: Boolean) {
        updateState { it.copy(isAssistantMuted = isMuted) }
        //viewModelScope.launch { preferencesManager.setAssistantMutedStatus(isMuted) }
        if (isMuted) {
            stopSpeaking()
        }
    }

    override fun onAssistantSpeechStopped() {
        if (uiState.value.isAssistantSpeaking) {
            stopSpeaking()
        }
    }

    override fun onStartListening() {
        if(uiState.value.isListening) {
            onStopTranscription()
        } else {
            onTranscribeUserQuestion()
        }
    }

    override fun onBackButtonClicked() {
        launchSideEffect(ChatSideEffects.CloseChat)
    }

    private fun onTranscribeUserQuestion() {
        updateState { it.copy(isListening = true) }
        executeUseCase(
            useCase = transcribeUserQuestionUseCase,
            onSuccess = ::onListenForTranscriptionCompleted,
            onMapExceptionToState = ::onMapExceptionToState,
            showLoadingState = false
        )
    }

    private fun onStopTranscription() {
        executeUseCase(
            useCase = endUserSpeechCaptureUseCase,
            onSuccess = { updateState { it.copy(isListening = false) } },
            onMapExceptionToState = ::onMapExceptionToState,
            showLoadingState = false
        )
    }

    private fun onListenForTranscriptionCompleted(transcription: String) {
        updateState { it.copy(isListening = false) }
        executeUseCaseWithParams(
            useCase = addInquizeMessageUseCase,
            params = AddInquizeMessageUseCase.Params(
                inquizeId = uiState.value.inquizeId,
                question = transcription
            ),
            onSuccess = ::onGetInquizeCompletedSuccessfully,
            onMapExceptionToState = ::onMapExceptionToState
        )
    }

    private fun onGetInquizeCompletedSuccessfully(inquizeBO: InquizeBO) {
        updateState { it.copy(inquizeId = inquizeBO.uid, messageList = inquizeBO.messages) }
        speakMessage(text = inquizeBO.messages.last().text)
    }

    private fun onMapExceptionToState(ex: Exception, uiState: ChatUiState) =
        uiState.copy(
            isLoading = false,
            errorMessage = errorMapper.mapToMessage(ex)
        )

    private fun speakMessage(text: String) {
        updateState { it.copy(isAssistantSpeaking = true) }
        executeUseCaseWithParams(
            useCase = textToSpeechUseCase,
            params = TextToSpeechUseCase.Params(text),
            onMapExceptionToState = ::onMapExceptionToState,
            onSuccess = {
                updateState { it.copy(isAssistantSpeaking = false) }
            },
            showLoadingState = false
        )
    }

    private fun stopSpeaking() {
        executeUseCase(useCase = stopTextToSpeechUseCase)
        updateState { it.copy(isAssistantSpeaking = false) }
    }
}


data class ChatUiState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val inquizeId: String = String.EMPTY,
    val infoMessage: String = String.EMPTY,
    val isAssistantResponseLoading: Boolean = false,
    val isAssistantMuted: Boolean = false,
    val isAssistantSpeaking: Boolean = false,
    val isListening: Boolean = false,
    val lastQuestion: String = String.EMPTY,
    val messageList: List<InquizeMessageBO> = emptyList()
): UiState<ChatUiState>(isLoading, errorMessage) {
    override fun copyState(isLoading: Boolean, errorMessage: String?): ChatUiState =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}


sealed interface ChatSideEffects: SideEffect {
    data object CloseChat: ChatSideEffects
}

