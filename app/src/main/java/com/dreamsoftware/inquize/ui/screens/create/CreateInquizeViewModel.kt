package com.dreamsoftware.inquize.ui.screens.create

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.brownie.core.BrownieViewModel
import com.dreamsoftware.brownie.core.SideEffect
import com.dreamsoftware.brownie.core.UiState
import com.dreamsoftware.brownie.utils.EMPTY
import com.dreamsoftware.inquize.domain.model.InquizeBO
import com.dreamsoftware.inquize.domain.usecase.CreateInquizeUseCase
import com.dreamsoftware.inquize.domain.usecase.TranscribeUserQuestionUseCase
import com.dreamsoftware.inquize.domain.usecase.EndUserSpeechCaptureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateInquizeViewModel @Inject constructor(
    private val transcribeUserQuestionUseCase: TranscribeUserQuestionUseCase,
    private val endUserSpeechCaptureUseCase: EndUserSpeechCaptureUseCase,
    private val createInquizeUseCase: CreateInquizeUseCase
) : BrownieViewModel<CreateInquizeUiState, CreateInquizeSideEffects>(), CreateInquizeScreenActionListener {

    companion object {
        private const val SHOW_CONFIRM_SCREEN_DELAY = 2000L
    }

    override fun onGetDefaultState(): CreateInquizeUiState = CreateInquizeUiState()

    fun onTranscribeUserQuestion(imageUrl: String) {
        updateState { it.copy(isListening = true, question = String.EMPTY, imageUrl = imageUrl) }
        executeUseCase(
            useCase = transcribeUserQuestionUseCase,
            onSuccess = ::onListenForTranscriptionCompleted,
            onMapExceptionToState = ::onMapExceptionToState,
            showLoadingState = false
        )
    }

    fun onCancelUserQuestion() {
        onResetState()
    }

    override fun onStartListening() {
        if(uiState.value.isListening) {
            onStopTranscription()
        } else {
            launchSideEffect(CreateInquizeSideEffects.StartListening)
        }
    }

    override fun onUpdateQuestion(newQuestion: String) {
        updateState { it.copy(question = newQuestion) }
    }

    override fun onCreateInquize() {
        with(uiState.value) {
            executeUseCaseWithParams(
                useCase = createInquizeUseCase,
                params = CreateInquizeUseCase.Params(imageUrl = imageUrl, question = question),
                onSuccess = ::onInquizeCreatedSuccessfully,
                onMapExceptionToState = ::onMapExceptionToState
            )
        }
    }

    override fun onCancelInquize() {
        onResetState()
    }

    private fun onStopTranscription() {
        executeUseCase(
            useCase = endUserSpeechCaptureUseCase,
            onSuccess = { onResetState() },
            onMapExceptionToState = ::onMapExceptionToState,
            showLoadingState = false
        )
    }

    private fun onListenForTranscriptionCompleted(transcription: String) {
        viewModelScope.launch {
            updateState { it.copy(isListening = false, question = transcription) }
            delay(SHOW_CONFIRM_SCREEN_DELAY)
            updateState { it.copy(showConfirm = true) }
        }
    }

    private fun onResetState() {
        updateState {
            it.copy(
                isListening = false,
                question = String.EMPTY,
                imageUrl = String.EMPTY,
                showConfirm = false
            )
        }
    }

    private fun onInquizeCreatedSuccessfully(data: InquizeBO) {
        onResetState()
        launchSideEffect(CreateInquizeSideEffects.InquizeCreated(data.uid))
    }

    private fun onMapExceptionToState(ex: Exception, uiState: CreateInquizeUiState) =
        uiState.copy(
            isLoading = false,
            isListening = false
        )
}

data class CreateInquizeUiState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val showConfirm: Boolean = false,
    val infoMessage: String = String.EMPTY,
    val isListening: Boolean = false,
    val imageUrl: String = String.EMPTY,
    val question: String = String.EMPTY
) : UiState<CreateInquizeUiState>(isLoading, errorMessage) {
    override fun copyState(isLoading: Boolean, errorMessage: String?): CreateInquizeUiState =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}

sealed interface CreateInquizeSideEffects : SideEffect {
    data object StartListening: CreateInquizeSideEffects
    data class InquizeCreated(val id: String): CreateInquizeSideEffects
}