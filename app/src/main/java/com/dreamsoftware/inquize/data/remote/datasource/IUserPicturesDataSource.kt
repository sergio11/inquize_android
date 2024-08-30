package com.dreamsoftware.inquize.data.remote.datasource

/**
 * Interface for managing user pictures saved as file paths (String).
 */
interface IUserPicturesDataSource {

    /**
     * Saves the image from the given file path [imagePath] to the store and returns the saved file path.
     * The [imageName] can be used to provide a custom name for the saved file.
     * Returns `null` if the image could not be saved.
     */
    suspend fun saveImage(imagePath: String, imageName: String): String?

    /**
     * Deletes the image referenced by the given [imageName].
     * Returns `true` if the image was deleted successfully, `false` otherwise.
     */
    suspend fun deleteImage(imageName: String): Boolean

    /**
     * Deletes all saved images from the store.
     * Returns `true` if all images were deleted successfully, `false` otherwise.
     */
    suspend fun deleteAllSavedImages(): Boolean
}
