package com.dreamsoftware.inquize.ui.home

import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dreamsoftware.inquize.R
import com.dreamsoftware.inquize.ui.components.AnimatedMicButtonWithTranscript
import com.dreamsoftware.inquize.ui.components.CameraPreview

@Composable
fun HomeScreen(
    cameraController: LifecycleCameraController,
    transcriptionText: String?,
    homeScreenUiState: HomeScreenUiState,
    onStartListening: () -> Unit,
    modifier: Modifier = Modifier
) {
    HomeScreen(
        modifier = modifier,
        cameraController = cameraController,
        userTextTranscription = transcriptionText,
        isListening = homeScreenUiState.isListening,
        onStartListening = onStartListening,
    )

}

@Composable
fun HomeScreen(
    cameraController: LifecycleCameraController,
    userTextTranscription: String?,
    isListening: Boolean,
    onStartListening: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            controller = cameraController
        )
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(16.dp),
            text = stringResource(R.string.app_name),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        AnimatedMicButtonWithTranscript(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(16.dp),
            userTextTranscription = userTextTranscription,
            isListening = isListening,
            onStartListening = onStartListening
        )
    }
}
