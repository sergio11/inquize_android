package com.dreamsoftware.inquize.data.remote.datasource.impl

import android.net.Uri
import com.dreamsoftware.inquize.data.remote.datasource.IUserPicturesDataSource
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

internal class UserPicturesDataSourceImpl(
    private val storage: FirebaseStorage
) : IUserPicturesDataSource {

    private companion object {
        const val STORAGE_BUCKET_NAME = "user_images"
    }

    private val storageRef by lazy {
        storage
            .reference
            .child(STORAGE_BUCKET_NAME)
    }

    /**
     * Saves the image from the given file path [imagePath] to Firebase Storage
     * and returns the download URL as a [String].
     * The [imageName] is used as the name for the saved file.
     * Returns `null` if the image could not be saved.
     */
    override suspend fun saveImage(imagePath: String, imageName: String): String? {
        return try {
            val fileUri = Uri.parse(imagePath) // Convert the String path to Uri
            val imageRef = storageRef.child(imageName)
            imageRef.putFile(fileUri).await() // Upload the file to Firebase Storage
            imageRef.downloadUrl.await().toString() // Retrieve the download URL as a String
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Deletes the image referenced by the given [imageName] from Firebase Storage.
     * Returns `true` if the image was deleted successfully, `false` otherwise.
     */
    override suspend fun deleteImage(imageName: String): Boolean = withContext(Dispatchers.IO) {
        try {
            storageRef
                .child(imageName)
                .delete()
                .await() // Delete the file from Firebase Storage
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Deletes all saved images from Firebase Storage.
     * Returns `true` if all images were deleted successfully, `false` otherwise.
     */
    override suspend fun deleteAllSavedImages(): Boolean = withContext(Dispatchers.IO) {
        try {
            // List all files in the 'user_images' folder and delete them
            val allFiles = storageRef.listAll().await()
            allFiles.items.forEach { it.delete().await() } // Deletes each file
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}