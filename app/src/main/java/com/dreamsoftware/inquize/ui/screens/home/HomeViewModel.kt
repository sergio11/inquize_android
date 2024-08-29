package com.dreamsoftware.inquize.ui.screens.home

import com.dreamsoftware.brownie.core.BrownieViewModel
import com.dreamsoftware.brownie.core.SideEffect
import com.dreamsoftware.brownie.core.UiState
import com.dreamsoftware.brownie.utils.EMPTY
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
class HomeViewModel @Inject constructor(
    private val transcribeUserQuestionUseCase: TranscribeUserQuestionUseCase,
    private val endUserSpeechCaptureUseCase: EndUserSpeechCaptureUseCase
) : BrownieViewModel<HomeUiState, HomeSideEffects>(),
    HomeScreenActionListener {
    override fun onGetDefaultState(): HomeUiState = HomeUiState()

    fun onTranscribeUserQuestion() {
        updateState { it.copy(isListening = true, userSpeechTranscription = String.EMPTY) }
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
            launchSideEffect(HomeSideEffects.StartListening)
        }
    }

    private fun onStopTranscription() {
        executeUseCase(
            useCase = endUserSpeechCaptureUseCase,
            onSuccess = { onResetState() },
            onMapExceptionToState = ::onMapExceptionToState
        )
    }

    private fun onMapExceptionToState(ex: Exception, uiState: HomeUiState) =
        uiState.copy(
            isLoading = false,
            isListening = false,
            userSpeechTranscription = String.EMPTY
        )

    private fun onListenForTranscriptionCompleted(transcription: String) {
        updateState { it.copy(isListening = false, userSpeechTranscription = transcription) }
    }

    private fun onResetState() {
        updateState { it.copy(isListening = false, userSpeechTranscription = String.EMPTY) }
    }
}

data class HomeUiState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val isListening: Boolean = false,
    val userSpeechTranscription: String = String.EMPTY
) : UiState<HomeUiState>(isLoading, errorMessage) {
    override fun copyState(isLoading: Boolean, errorMessage: String?): HomeUiState =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}

sealed interface HomeSideEffects : SideEffect {
    data object StartListening: HomeSideEffects
}