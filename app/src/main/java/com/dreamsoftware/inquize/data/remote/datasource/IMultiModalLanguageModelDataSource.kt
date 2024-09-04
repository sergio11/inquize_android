package com.dreamsoftware.inquize.data.remote.datasource

import com.dreamsoftware.inquize.data.remote.dto.ResolveQuestionDTO
import com.dreamsoftware.inquize.data.remote.exception.GenerateImageDescriptionRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.ResolveQuestionFromContextRemoteDataException

/**
 * Represents a remote multi-modal language model datasource
 */
interface IMultiModalLanguageModelDataSource {

    @Throws(ResolveQuestionFromContextRemoteDataException::class)
    suspend fun resolveQuestionFromContext(data: ResolveQuestionDTO): String

    @Throws(GenerateImageDescriptionRemoteDataException::class)
    suspend fun generateImageDescription(imageUrl: String): String
}
