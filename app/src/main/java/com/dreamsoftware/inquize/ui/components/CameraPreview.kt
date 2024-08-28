package com.dreamsoftware.inquize.ui.components

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView

/**
 * A camera preview composable that uses [PreviewView] under the hood.
 * @param controller The [LifecycleCameraController] instance that controls the camera.
 * @param modifier The [Modifier] to be applied to this composable.
 */
@Composable
fun CameraPreview(modifier: Modifier = Modifier, controller: LifecycleCameraController) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        modifier = modifier,
        factory = { context ->
            PreviewView(context).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
            }
        }
    )
}
