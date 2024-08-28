package com.dreamsoftware.inquize.data.local.bitmapstore

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import com.dreamsoftware.inquize.di.IODispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.UUID
import javax.inject.Inject

class AndroidBitmapStore @Inject constructor(
    @ApplicationContext private val context: Context,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : BitmapStore {
    private val cacheDir = context.cacheDir

    override suspend fun saveBitmap(bitmap: Bitmap): Uri? = withContext(ioDispatcher) {
        try {
            val byteArray = ByteArrayOutputStream().use { byteArrayOutputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                byteArrayOutputStream.toByteArray()
            }
            return@withContext File(
                cacheDir,
                "$PERCEIVE_IMAGE_FILE_PREFIX${UUID.randomUUID()}.$IMAGE_FILE_EXTENSION"
            )
                .apply { writeBytes(byteArray) }
                .toUri()
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            null
        }
    }

    override suspend fun retrieveBitmapForUri(uri: Uri): Bitmap? = withContext(ioDispatcher) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            null
        }
    }

    override suspend fun deleteBitmapWithUri(uri: Uri): Boolean = withContext(ioDispatcher) {
        try {
            uri.path?.let(::File)?.delete() ?: true
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            false
        }
    }

    override suspend fun deleteAllSavedBitmaps() = withContext(ioDispatcher) {
        val files = cacheDir.listFiles() ?: return@withContext false

        for (file in files) {
            if (file.extension != IMAGE_FILE_EXTENSION) continue
            if (!file.name.startsWith(PERCEIVE_IMAGE_FILE_PREFIX)) continue

            val wasSuccessfullyDeleted = file.delete()
            if (!wasSuccessfullyDeleted) return@withContext false
        }
        return@withContext true
    }

    companion object {
        /**
         * Common prefix for image files saved by the app.
         */
        private const val PERCEIVE_IMAGE_FILE_PREFIX = "perceive_app_"

        /**
         * Image file extension for image files saved by the app.
         */
        private const val IMAGE_FILE_EXTENSION = "jpeg"
    }
}