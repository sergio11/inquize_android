package com.dreamsoftware.inquize.ui.screens.create

import android.net.Uri
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.brownie.component.screen.BrownieScreen
import com.dreamsoftware.inquize.utils.takePicture

@Composable
fun CreateInquizeScreen(
    viewModel: CreateInquizeViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val cameraController = remember { LifecycleCameraController(context) }
    val onEndOfSpeech = remember {
        { transcription: String, associatedBitmapUri: Uri ->
            /*navController.navigate(
                PerceiveNavigationDestinations
                    .ChatScreen
                    .buildRoute(transcription, associatedBitmapUri)
            )*/
        }
    }

    BrownieScreen(
        viewModel = viewModel,
        onBackPressed = onBackPressed,
        onInitialUiState = { CreateInquizeUiState() },
        onSideEffect = {
            when(it) {
                CreateInquizeSideEffects.StartListening -> {
                    cameraController.takePicture(
                        context = context,
                        onSuccess = {
                            onTranscribeUserQuestion()
                        },
                        onError = {
                            onCancelUserQuestion()
                        }
                    )
                }
            }
        }
    ) { uiState ->
        CreateInquizeScreenContent(
            uiState = uiState,
            actionListener = viewModel,
            lifecycleCameraController = cameraController
        )
    }
}
