package com.dreamsoftware.inquize.domain.repository

import com.dreamsoftware.inquize.domain.exception.DeleteInquizeByIdException
import com.dreamsoftware.inquize.domain.exception.FetchAllInquizeException
import com.dreamsoftware.inquize.domain.exception.FetchInquizeByIdException
import com.dreamsoftware.inquize.domain.exception.SaveInquizeException
import com.dreamsoftware.inquize.domain.model.InquizeBO
import com.dreamsoftware.inquize.domain.model.SaveInquizeBO

interface IInquizeRepository {

    @Throws(SaveInquizeException::class)
    suspend fun save(data: SaveInquizeBO): InquizeBO

    @Throws(DeleteInquizeByIdException::class)
    suspend fun deleteById(userId: String, id: String)

    @Throws(FetchInquizeByIdException::class)
    suspend fun fetchById(userId: String, id: String): InquizeBO

    @Throws(FetchAllInquizeException::class)
    suspend fun fetchAllByUserId(userId: String): List<InquizeBO>
}