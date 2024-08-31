package com.dreamsoftware.inquize.ui.screens.create

import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dreamsoftware.inquize.R
import com.dreamsoftware.inquize.ui.components.AnimatedMicButtonWithTranscript
import com.dreamsoftware.inquize.ui.components.CameraPreview

@Composable
internal fun CreateInquizeScreenContent(
    uiState: CreateInquizeUiState,
    actionListener: CreateInquizeScreenActionListener,
    lifecycleCameraController: LifecycleCameraController
) {
    with(uiState) {
        Box {
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                controller = lifecycleCameraController
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(0.4f))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.main_logo_inverse),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
            AnimatedMicButtonWithTranscript(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(16.dp),
                userTextTranscription = userSpeechTranscription,
                isListening = isListening,
                onStartListening = actionListener::onStartListening
            )
        }
    }
}