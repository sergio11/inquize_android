package com.dreamsoftware.inquize.data.remote.languagemodel

import android.graphics.Bitmap


/**
 * Represents a remote multi-modal language model.
 */
interface MultiModalLanguageModelClient {
    /**
     * Starts a new chat session.
     */
    fun startNewChatSession()

    /**
     * Sends a message to the model and receives a response.
     *
     * @param messageContents A list of [MessageContent]to be sent to the model.
     *
     * @return A [Result] containing the model's response as a string, or an error if the message could not be sent.
     */
    suspend fun sendMessage(messageContents: List<MessageContent>): Result<String>

    /**
     * Ends the current chat session.
     */
    fun endChatSession()

    /**
     * Represents the content of a message in a chat session.
     */
    sealed interface MessageContent {
        /**
         * Represents an image message.
         *
         * @property bitmap The image data as a [Bitmap].
         */
        data class Image(val bitmap: Bitmap) : MessageContent

        /**
         * Represents a text message.
         *
         * @property text The content of the message.
         */
        data class Text(val text: String) : MessageContent

    }
}
