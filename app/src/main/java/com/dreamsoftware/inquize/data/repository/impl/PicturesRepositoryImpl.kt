package com.dreamsoftware.inquize.data.repository.impl

import com.dreamsoftware.inquize.data.remote.datasource.IUserPicturesDataSource
import com.dreamsoftware.inquize.data.remote.exception.DeletePictureRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.SavePictureRemoteDataException
import com.dreamsoftware.inquize.data.repository.impl.core.SupportRepositoryImpl
import com.dreamsoftware.inquize.domain.exception.DeletePictureException
import com.dreamsoftware.inquize.domain.exception.SavePictureException
import com.dreamsoftware.inquize.domain.repository.IPicturesRepository
import kotlinx.coroutines.CoroutineDispatcher

internal class PicturesRepositoryImpl(
    private val userPicturesDataSource: IUserPicturesDataSource,
    dispatcher: CoroutineDispatcher
): SupportRepositoryImpl(dispatcher), IPicturesRepository {

    @Throws(SavePictureException::class)
    override suspend fun save(imagePath: String, imageName: String): String = safeExecute {
        try {
            userPicturesDataSource.saveImage(imagePath, imageName)
        } catch (ex: SavePictureRemoteDataException) {
            throw SavePictureException("An error occurred when trying to save a new picture", ex)
        }
    }

    @Throws(DeletePictureException::class)
    override suspend fun delete(imageName: String) = safeExecute {
        try {
            userPicturesDataSource.deleteImage(imageName)
        } catch (ex: DeletePictureRemoteDataException) {
            throw DeletePictureException("An error occurred when trying to delete the picture: $imageName", ex)
        }
    }
}