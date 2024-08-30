package com.dreamsoftware.inquize.domain.repository

import com.dreamsoftware.inquize.domain.exception.SaveInquizeException
import com.dreamsoftware.inquize.domain.model.InquizeBO
import com.dreamsoftware.inquize.domain.model.SaveInquizeBO

interface IInquizeRepository {

    @Throws(SaveInquizeException::class)
    suspend fun save(data: SaveInquizeBO): InquizeBO
}