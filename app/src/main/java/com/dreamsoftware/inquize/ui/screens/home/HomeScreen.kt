package com.dreamsoftware.inquize.ui.screens.home

import android.net.Uri
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.brownie.component.screen.BrownieScreen
import com.dreamsoftware.inquize.utils.takePicture

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onStartListening: () -> Unit,
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
        onInitialUiState = { HomeUiState() },
        onSideEffect = {
            when(it) {
                HomeSideEffects.StartListening -> {
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
        HomeScreenContent(
            uiState = uiState,
            actionListener = viewModel,
            lifecycleCameraController = cameraController
        )
    }
}
