package com.dreamsoftware.inquize.ui.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.annotation.StringRes
import androidx.camera.core.ImageProxy
import com.dreamsoftware.inquize.R

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

fun Context.shareApp(@StringRes shareMessageRes: Int) {
    startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, getString(shareMessageRes))
    }, "Share app link"))
}