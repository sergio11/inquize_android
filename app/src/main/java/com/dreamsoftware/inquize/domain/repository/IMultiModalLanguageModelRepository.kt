package com.dreamsoftware.inquize.domain.repository

import com.dreamsoftware.inquize.domain.model.ResolveQuestionBO

interface IMultiModalLanguageModelRepository {

    suspend fun resolveQuestion(data: ResolveQuestionBO): String
}