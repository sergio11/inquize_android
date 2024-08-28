package com.dreamsoftware.inquize.ui.screens.home

import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.inquize.data.local.bitmapstore.BitmapStore
import com.dreamsoftware.inquize.domain.service.ITranscriptionService

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
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
}