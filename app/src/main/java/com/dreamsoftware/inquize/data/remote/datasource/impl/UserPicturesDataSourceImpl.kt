package com.dreamsoftware.inquize.data.remote.datasource.impl

import android.net.Uri
import com.dreamsoftware.inquize.data.remote.datasource.IUserPicturesDataSource
import com.dreamsoftware.inquize.data.remote.exception.DeleteAllPicturesRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.DeletePictureRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.SavePictureRemoteDataException
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

internal class UserPicturesDataSourceImpl(
    private val storage: FirebaseStorage,
    private val dispatcher: CoroutineDispatcher
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
    @Throws(SavePictureRemoteDataException::class)
    override suspend fun saveImage(imagePath: String, imageName: String): String = withContext(dispatcher) {
        try {
            val fileUri = Uri.parse(imagePath) // Convert the String path to Uri
            storageRef.child(imageName).run {
                putFile(fileUri).await() // Upload the file to Firebase Storage
                downloadUrl.await().toString() // Retrieve the download URL as a String
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw SavePictureRemoteDataException("An error occurred when trying to upload picture saved at $imagePath")
        }
    }

    /**
     * Deletes the image referenced by the given [imageName] from Firebase Storage.
     * Returns `true` if the image was deleted successfully, `false` otherwise.
     */
    @Throws(DeletePictureRemoteDataException::class)
    override suspend fun deleteImage(imageName: String): Unit = withContext(dispatcher) {
        try {
            storageRef
                .child(imageName)
                .delete()
                .await() // Delete the file from Firebase Storage
        } catch (e: Exception) {
            e.printStackTrace()
            throw DeletePictureRemoteDataException("An error occurred when trying to delete picture named as $imageName")
        }
    }

    /**
     * Deletes all saved images from Firebase Storage.
     * Returns `true` if all images were deleted successfully, `false` otherwise.
     */
    @Throws(DeleteAllPicturesRemoteDataException::class)
    override suspend fun deleteAllSavedImages(): Unit = withContext(dispatcher) {
        try {
            // List all files in the 'user_images' folder and delete them
            val allFiles = storageRef.listAll().await()
            allFiles.items.forEach { it.delete().await() } // Deletes each file
        } catch (e: Exception) {
            e.printStackTrace()
            throw DeleteAllPicturesRemoteDataException("An error occurred when trying to delete all the user pictures")
        }
    }
}