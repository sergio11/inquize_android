package com.dreamsoftware.inquize.data.remote.datasource

import com.dreamsoftware.inquize.data.remote.dto.ResolveQuestionDTO

/**
 * Represents a remote multi-modal language model datasource
 */
interface IMultiModalLanguageModelDataSource {

    suspend fun resolveQuestion(data: ResolveQuestionDTO): String
}
