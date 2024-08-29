package com.dreamsoftware.inquize.data.remote.datasource.impl

import android.graphics.Bitmap
import android.util.Log
import com.dreamsoftware.inquize.data.remote.datasource.IMultiModalLanguageModelDataSource
import com.dreamsoftware.inquize.data.remote.dto.QuestionWithImageDTO
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CancellationException
import java.lang.IllegalStateException

internal class GeminiLanguageModelDataSourceImpl(
    private val generativeTextModel: GenerativeModel,
    private val generativeMultiModalModel: GenerativeModel
) : IMultiModalLanguageModelDataSource {

    private companion object {
        const val USER = "user"
        const val MODEL = "model"

        const val CHAT_SESSION_INVALID_MESSAGE =
            "A chat session is not in progress. Please use startNewChat() before attempting to send new messages."
    }

    private var currentChatSession: Chat? = null

    override fun startNewChatSession() {
        currentChatSession = generativeTextModel.startChat()
    }

    override suspend fun sendMessage(data: QuestionWithImageDTO): Result<String> {
        val currentChatSession =
            currentChatSession ?: throw IllegalStateException(CHAT_SESSION_INVALID_MESSAGE)
        Log.d("ATV_CHANGES", "GeminiLanguageModelClient sendMessage called with question and image")
        return try {
            // Generate image description if image is provided
            val imageDescription = generateImageDescription(data.image)
            // Generate prompt combining the image description and the user's question
            val prompt = generatePrompt(data.question, imageDescription)
            // Send the message and return the response
            Result.success(currentChatSession.sendMessage(prompt).text!!)
        } catch (exception: Exception) {
            Log.d("ATV_CHANGES", "GeminiLanguageModelClient exception: $exception")
            if (exception is CancellationException) throw exception
            Result.failure(exception)
        }
    }

    override fun endChatSession() {
        currentChatSession = null
    }

    private suspend fun generateImageDescription(image: Bitmap): String? = try {
        generativeMultiModalModel.generateContent(content(USER) {
            image(image)
            text("Provide a detailed description of the contents of the image.")
        }).text
    } catch (exception: Exception) {
        Log.d("ATV_CHANGES", "Error generating image description: $exception")
        null // Return null if the description generation fails
    }

    private fun generatePrompt(question: String, imageDescription: String?): Content =
        content(USER) {
            imageDescription?.let {
                text(
                    """
                        |You are a highly knowledgeable and perceptive assistant. A user is showing you something 
                        |and asking questions about it. Based on the detailed description provided below, 
                        |generate a response as if you are physically present, observing the object alongside the user.
                        |
                        |Your answers should be conversational, engaging, and insightful. Focus on providing 
                        |accurate and relevant information that enhances the user's understanding. Make sure 
                        |your responses are clear and feel natural, as if you are guiding the user through 
                        |their observations with empathy and curiosity. 
                        |
                        |Avoid directly referencing that you are generating responses based on a description. 
                        |Instead, convey your expertise as if you are directly interacting with the object in real-time.
                    """.trimMargin()
                )
                text(it)
            }
            text(question)
        }
}