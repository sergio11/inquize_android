package com.dreamsoftware.inquize.data.local.bitmapstore

import android.graphics.Bitmap
import android.net.Uri


/**
 * A store for saving and retrieving [Bitmap]s.
 */
interface BitmapStore {
    /**
     * Saves the given [bitmap] and returns the [Uri] of the saved bitmap.
     * Returns `null` if the bitmap could not be saved.
     */
    suspend fun saveBitmap(bitmap: Bitmap): Uri?

    /**
     * Retrieves a [Bitmap] for the given [uri].
     * Returns `null` if the bitmap could not be retrieved or if it doesn't exist.
     */
    suspend fun retrieveBitmapForUri(uri: Uri): Bitmap?

    /**
     * Deletes the bitmap with the given [uri].
     * Returns `true` if the bitmap was deleted successfully, `false` otherwise.
     */
    suspend fun deleteBitmapWithUri(uri: Uri): Boolean

    /**
     * Deletes all saved bitmaps.
     * Returns `true` if all bitmaps were deleted successfully, `false` otherwise.
     */
    suspend fun deleteAllSavedBitmaps(): Boolean
}