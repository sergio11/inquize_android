package com.dreamsoftware.inquize.data.remote.datasource

import com.dreamsoftware.inquize.data.remote.exception.DeleteAllPicturesRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.DeletePictureRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.SavePictureRemoteDataException

/**
 * Interface for managing user pictures saved as file paths (String).
 */
interface IUserPicturesDataSource {

    /**
     * Saves the image from the given file path [imagePath] to the store and returns the saved file path.
     * The [imageName] can be used to provide a custom name for the saved file.
     */
    @Throws(SavePictureRemoteDataException::class)
    suspend fun saveImage(imagePath: String, imageName: String): String

    /**
     * Deletes the image referenced by the given [imageName].
     */
    @Throws(DeletePictureRemoteDataException::class)
    suspend fun deleteImage(imageName: String)

    /**
     * Deletes all saved images from the store.
     */
    @Throws(DeleteAllPicturesRemoteDataException::class)
    suspend fun deleteAllSavedImages()
}
