package com.dreamsoftware.inquize.ui.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageProxy

fun ImageProxy.correctOrientation(): Bitmap =
    toBitmap().run {
        Bitmap.createBitmap(
            this,
            0,
            0,
            width,
            height,
            Matrix().apply { postRotate(imageInfo.rotationDegrees.toFloat()) },
            false
        )
    }