package com.dreamsoftware.inquize.domain.repository

import com.dreamsoftware.inquize.domain.exception.DeletePictureException
import com.dreamsoftware.inquize.domain.exception.SavePictureException

interface IPicturesRepository {

    @Throws(SavePictureException::class)
    suspend fun save(imagePath: String, imageName: String): String

    @Throws(DeletePictureException::class)
    suspend fun delete(imageName: String)
}