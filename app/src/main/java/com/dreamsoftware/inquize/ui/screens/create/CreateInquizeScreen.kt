package com.dreamsoftware.inquize.ui.screens.create

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
                            onTranscribeUserQuestion(imageUrl = it)
                        },
                        onError = {
                            onCancelUserQuestion()
                        }
                    )
                }
                is CreateInquizeSideEffects.InquizeCreated -> onBackPressed()
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
