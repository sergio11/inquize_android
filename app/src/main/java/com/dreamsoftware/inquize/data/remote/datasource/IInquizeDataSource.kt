package com.dreamsoftware.inquize.data.remote.datasource

import com.dreamsoftware.inquize.data.remote.dto.AddInquizeMessageDTO
import com.dreamsoftware.inquize.data.remote.dto.CreateInquizeDTO
import com.dreamsoftware.inquize.data.remote.dto.InquizeDTO
import com.dreamsoftware.inquize.data.remote.exception.AddInquizeMessageRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.DeleteInquizeByIdRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.FetchAllInquizeRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.FetchInquizeByIdRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.CreateInquizeRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.SearchInquizeRemoteDataException

interface IInquizeDataSource {

    @Throws(SearchInquizeRemoteDataException::class)
    suspend fun search(userId: String, term: String): List<InquizeDTO>

    @Throws(CreateInquizeRemoteDataException::class)
    suspend fun create(data: CreateInquizeDTO)

    @Throws(AddInquizeMessageRemoteDataException::class)
    suspend fun addMessage(data: AddInquizeMessageDTO)

    @Throws(FetchInquizeByIdRemoteDataException::class)
    suspend fun fetchById(userId: String, id: String): InquizeDTO

    @Throws(FetchAllInquizeRemoteDataException::class)
    suspend fun fetchAllByUserId(userId: String): List<InquizeDTO>

    @Throws(DeleteInquizeByIdRemoteDataException::class)
    suspend fun deleteById(userId: String, id: String)
}