package com.dreamsoftware.inquize.data.remote.datasource

import com.dreamsoftware.inquize.data.remote.dto.QuestionWithImageDTO

/**
 * Represents a remote multi-modal language model datasource
 */
interface IMultiModalLanguageModelDataSource {
    /**
     * Starts a new chat session.
     */
    fun startNewChatSession()

    suspend fun sendMessage(data: QuestionWithImageDTO): Result<String>

    /**
     * Ends the current chat session.
     */
    fun endChatSession()
}
