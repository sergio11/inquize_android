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
import com.dreamsoftware.inquize.domain.usecase.GetAssistantMutedStatusUseCase
import com.dreamsoftware.inquize.domain.usecase.GetInquizeByIdUseCase
import com.dreamsoftware.inquize.domain.usecase.StopTextToSpeechUseCase
import com.dreamsoftware.inquize.domain.usecase.TextToSpeechUseCase
import com.dreamsoftware.inquize.domain.usecase.TranscribeUserQuestionUseCase
import com.dreamsoftware.inquize.domain.usecase.UpdateAssistantMutedStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getInquizeByIdUseCase: GetInquizeByIdUseCase,
    private val transcribeUserQuestionUseCase: TranscribeUserQuestionUseCase,
    private val endUserSpeechCaptureUseCase: EndUserSpeechCaptureUseCase,
    private val textToSpeechUseCase: TextToSpeechUseCase,
    private val stopTextToSpeechUseCase: StopTextToSpeechUseCase,
    private val addInquizeMessageUseCase: AddInquizeMessageUseCase,
    private val updateAssistantMutedStatusUseCase: UpdateAssistantMutedStatusUseCase,
    private val getAssistantMutedStatusUseCase: GetAssistantMutedStatusUseCase,
    @ChatErrorMapper private val errorMapper: IBrownieErrorMapper
) : BrownieViewModel<ChatUiState, ChatSideEffects>(), ChatScreenActionListener {

    fun load(id: String) {
        executeUseCase(
            useCase = getAssistantMutedStatusUseCase,
            onSuccess = ::onGetAssistantMutedStatusCompleted,
            showLoadingState = false
        )
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
        executeUseCaseWithParams(
            useCase = updateAssistantMutedStatusUseCase,
            params = UpdateAssistantMutedStatusUseCase.Params(isAssistantMuted = isMuted),
            showLoadingState = false
        )
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

    override fun onCleared() {
        doOnUiState {
            if(isAssistantSpeaking) {
                stopSpeaking()
            }
            if(isListening) {
                onStopTranscription()
            }
        }
        super.onCleared()
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
        doOnUiState {
            if(!isAssistantMuted) {
                speakMessage(text = inquizeBO.messages.last().text)
            }
        }
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

    private fun onGetAssistantMutedStatusCompleted(isMuted: Boolean) {
        updateState { it.copy(isAssistantMuted = isMuted) }
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

