package com.dreamsoftware.inquize.data.remote.datasource

import com.dreamsoftware.inquize.data.remote.dto.SaveInquizeDTO
import com.dreamsoftware.inquize.data.remote.dto.InquizeDTO
import com.dreamsoftware.inquize.data.remote.exception.DeleteInquizeByIdRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.FetchAllInquizeRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.FetchInquizeByIdRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.SaveInquizeRemoteDataException

interface IInquizeDataSource {

    @Throws(SaveInquizeRemoteDataException::class)
    suspend fun save(data: SaveInquizeDTO)

    @Throws(FetchInquizeByIdRemoteDataException::class)
    suspend fun fetchById(userId: String, id: String): InquizeDTO

    @Throws(FetchAllInquizeRemoteDataException::class)
    suspend fun fetchAllByUserId(userId: String): List<InquizeDTO>

    @Throws(DeleteInquizeByIdRemoteDataException::class)
    suspend fun deleteById(userId: String, id: String)
}