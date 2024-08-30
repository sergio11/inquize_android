package com.dreamsoftware.inquize.data.remote.datasource.impl

import android.net.Uri
import com.dreamsoftware.inquize.data.remote.datasource.IImageDataSource
import com.dreamsoftware.inquize.data.remote.exception.DeletePictureRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.SavePictureRemoteDataException
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

internal class ImageDataSourceImpl(
    private val storage: FirebaseStorage,
    private val dispatcher: CoroutineDispatcher
) : IImageDataSource {

    private companion object {
        const val STORAGE_BUCKET_NAME = "user_images"
    }

    private val storageRef by lazy {
        storage
            .reference
            .child(STORAGE_BUCKET_NAME)
    }

    /**
     * Saves the image from the given file path [path] to Firebase Storage
     * and returns the download URL as a [String].
     * The [name] is used as the name for the saved file.
     * Returns `null` if the image could not be saved.
     */
    @Throws(SavePictureRemoteDataException::class)
    override suspend fun save(path: String, name: String): String = withContext(dispatcher) {
        try {
            val fileUri = Uri.parse(path) // Convert the String path to Uri
            storageRef.child(name).run {
                putFile(fileUri).await() // Upload the file to Firebase Storage
                downloadUrl.await().toString() // Retrieve the download URL as a String
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw SavePictureRemoteDataException("An error occurred when trying to upload picture saved at $path")
        }
    }

    /**
     * Deletes the image referenced by the given [name] from Firebase Storage.
     * Returns `true` if the image was deleted successfully, `false` otherwise.
     */
    @Throws(DeletePictureRemoteDataException::class)
    override suspend fun deleteByName(name: String): Unit = withContext(dispatcher) {
        try {
            storageRef
                .child(name)
                .delete()
                .await() // Delete the file from Firebase Storage
        } catch (e: Exception) {
            e.printStackTrace()
            throw DeletePictureRemoteDataException("An error occurred when trying to delete picture named as $name")
        }
    }
}