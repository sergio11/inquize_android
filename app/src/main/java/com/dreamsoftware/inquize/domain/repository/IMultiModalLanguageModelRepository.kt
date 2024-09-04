package com.dreamsoftware.inquize.domain.repository

import com.dreamsoftware.inquize.domain.exception.GenerateImageDescriptionException
import com.dreamsoftware.inquize.domain.exception.ResolveQuestionFromContextException
import com.dreamsoftware.inquize.domain.model.ResolveQuestionBO

interface IMultiModalLanguageModelRepository {

    @Throws(ResolveQuestionFromContextException::class)
    suspend fun resolveQuestion(data: ResolveQuestionBO): String

    @Throws(GenerateImageDescriptionException::class)
    suspend fun generateImageDescription(imageUrl: String): String
}