package com.dreamsoftware.inquize.utils

import android.content.Context
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import android.net.Uri
import androidx.core.content.ContextCompat
import java.io.File

/**
 * Captures a photo and saves it to a local file.
 *
 * @param context The context used for file operations and obtaining the main executor.
 * @param onSuccess Callback invoked when the image is successfully saved. Provides the Uri of the saved image.
 * @param onError Callback invoked when an error occurs during image capture. Provides the ImageCaptureException.
 */
fun LifecycleCameraController.takePicture(
    context: Context,
    onSuccess: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    // Create a file to store the captured image
    val imageFile = createImageFile(context)

    // Build output options for the image capture, specifying the file to save to
    val outputFileOptions = ImageCapture.OutputFileOptions.Builder(imageFile).build()

    // Capture the image and save it to the specified file
    this.takePicture(
        outputFileOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            // Handle any errors that occur during the capture process
            override fun onError(exception: ImageCaptureException) = onError(exception)

            // Called when the image is successfully saved
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                // Convert the saved file to a Uri and pass it to the onSuccess callback
                val savedUri = Uri.fromFile(imageFile)
                onSuccess(savedUri)
            }
        }
    )
}

/**
 * Creates a temporary image file in the external files directory.
 *
 * @param context The context used to access the external files directory.
 * @return A File object pointing to the newly created image file.
 */
private fun createImageFile(context: Context): File {
    val storageDir = context.getExternalFilesDir("images")
    return File.createTempFile(
        "IMG_${System.currentTimeMillis()}_", // Prefix for the file name, ensuring uniqueness
        ".jpg", // Suffix for the file extension
        storageDir // Directory where the file will be stored
    )
}