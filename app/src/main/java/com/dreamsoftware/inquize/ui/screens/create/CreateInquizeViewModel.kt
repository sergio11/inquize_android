package com.dreamsoftware.inquize.ui.screens.create

import android.util.Log
import com.dreamsoftware.brownie.core.BrownieViewModel
import com.dreamsoftware.brownie.core.SideEffect
import com.dreamsoftware.brownie.core.UiState
import com.dreamsoftware.brownie.utils.EMPTY
import com.dreamsoftware.inquize.domain.model.InquizeBO
import com.dreamsoftware.inquize.domain.usecase.CreateInquizeUseCase
import com.dreamsoftware.inquize.domain.usecase.TranscribeUserQuestionUseCase
import com.dreamsoftware.inquize.domain.usecase.EndUserSpeechCaptureUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ITranscriptionService: ITranscriptionService,
    private val bitmapStore: BitmapStore
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState as StateFlow<HomeScreenUiState>

    private val _userSpeechTranscriptionStream = MutableStateFlow<String?>(null)
    val userSpeechTranscriptionStream = _userSpeechTranscriptionStream as StateFlow<String?>

    fun startTranscription(
        currentCameraImageProxy: ImageProxy,
        onEndOfSpeech: (transcription: String, associatedBitmapUri: Uri) -> Unit
    ) {
        _userSpeechTranscriptionStream.update { "" }
        // use a completely new state to remove old stale states
        _uiState.update { HomeScreenUiState(isListening = true) }
        ITranscriptionService.startListening(
            transcription = { transcription -> _userSpeechTranscriptionStream.update { transcription } },
            onEndOfSpeech = {
                viewModelScope.launch {
                    val transcription =
                        _userSpeechTranscriptionStream.value ?: return@launch // todo:error handling
                    val associatedBitmapUri =
                        rotateImageProxyToCorrectOrientation(currentCameraImageProxy)
                            ?: return@launch // todo: error handling
                    // reset states to defaults
                    _uiState.update { HomeScreenUiState() }
                    _userSpeechTranscriptionStream.value = null
                    // invoke callback in the main thread
                    withContext(Dispatchers.Main.immediate) {
                        onEndOfSpeech(transcription, associatedBitmapUri)
                    }
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

    private suspend fun rotateImageProxyToCorrectOrientation(imageProxy: ImageProxy): Uri? {
        val rotationDegreesToMakeImageUpright = imageProxy.imageInfo.rotationDegrees.toFloat()
        val bitmap = imageProxy.toBitmap().run {
            Bitmap.createBitmap(
                this,
                0,
                0,
                width,
                height,
                Matrix().apply { postRotate(rotationDegreesToMakeImageUpright) },
                false
            )
        }
        return bitmapStore.saveBitmap(bitmap)
    }
}*/

@HiltViewModel
class CreateInquizeViewModel @Inject constructor(
    private val transcribeUserQuestionUseCase: TranscribeUserQuestionUseCase,
    private val endUserSpeechCaptureUseCase: EndUserSpeechCaptureUseCase,
    private val createInquizeUseCase: CreateInquizeUseCase
) : BrownieViewModel<CreateInquizeUiState, CreateInquizeSideEffects>(),
    CreateInquizeScreenActionListener {
    override fun onGetDefaultState(): CreateInquizeUiState = CreateInquizeUiState()

    fun onTranscribeUserQuestion(imageUrl: String) {
        updateState { it.copy(isListening = true, question = String.EMPTY, imageUrl = imageUrl) }
        executeUseCase(
            useCase = transcribeUserQuestionUseCase,
            onSuccess = ::onListenForTranscriptionCompleted,
            onMapExceptionToState = ::onMapExceptionToState
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
            onMapExceptionToState = ::onMapExceptionToState
        )
    }

    private fun onListenForTranscriptionCompleted(transcription: String) {
        updateState { it.copy(isListening = false, question = transcription) }
    }

    private fun onResetState() {
        updateState { it.copy(isListening = false, question = String.EMPTY, imageUrl = String.EMPTY) }
    }

    private fun onInquizeCreatedSuccessfully(data: InquizeBO) {
        Log.d("ATV_INQUIZE_CREATED", "onInquizeCreatedSuccessfully $data")
        launchSideEffect(CreateInquizeSideEffects.InquizeCreated(data.uid))
    }

    private fun onMapExceptionToState(ex: Exception, uiState: CreateInquizeUiState) =
        uiState.copy(
            isLoading = false,
            isListening = false,
            imageUrl = String.EMPTY,
            question = String.EMPTY
        )
}

data class CreateInquizeUiState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
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