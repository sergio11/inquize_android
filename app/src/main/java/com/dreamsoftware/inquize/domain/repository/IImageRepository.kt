package com.dreamsoftware.inquize.domain.repository

import com.dreamsoftware.inquize.domain.exception.DeletePictureException
import com.dreamsoftware.inquize.domain.exception.SavePictureException

interface IImageRepository {

    @Throws(SavePictureException::class)
    suspend fun save(path: String, name: String): String

    @Throws(DeletePictureException::class)
    suspend fun deleteByName(name: String)
}