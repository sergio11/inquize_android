package com.dreamsoftware.inquize.data.remote.datasource.impl

import android.net.Uri
import com.dreamsoftware.inquize.data.remote.datasource.IImageDataSource
import com.dreamsoftware.inquize.data.remote.datasource.impl.core.SupportDataSourceImpl
import com.dreamsoftware.inquize.data.remote.exception.DeletePictureRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.SavePictureRemoteDataException
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileNotFoundException

internal class ImageDataSourceImpl(
    private val storage: FirebaseStorage,
    dispatcher: CoroutineDispatcher
) : SupportDataSourceImpl(dispatcher), IImageDataSource {

    private companion object {
        const val STORAGE_BUCKET_NAME = "user_inquize"
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
    override suspend fun save(path: String, name: String): String = safeExecution(
        onExecuted = {
            val file = File(path)
            if (!file.exists()) {
                throw FileNotFoundException("File not found at path: $path")
            }
            val fileUri = Uri.fromFile(file) // Use fromFile to create a valid Uri from File
            storageRef.child(name).run {
                putFile(fileUri).await() // Upload the file to Firebase Storage
                downloadUrl.await().toString() // Retrieve the download URL as a String
            }
        },
        onErrorOccurred = { ex ->
            SavePictureRemoteDataException("An error occurred when trying to upload picture saved at $path", ex)
        }
    )

    /**
     * Deletes the image referenced by the given [name] from Firebase Storage.
     * Returns `true` if the image was deleted successfully, `false` otherwise.
     */
    @Throws(DeletePictureRemoteDataException::class)
    override suspend fun deleteByName(name: String): Unit = safeExecution(
        onExecuted = {
            storageRef
                .child(name)
                .delete()
                .await() // Delete the file from Firebase Storage
        },
        onErrorOccurred = { ex ->
            DeletePictureRemoteDataException("An error occurred when trying to delete picture named as $name", ex)
        }
    )
}