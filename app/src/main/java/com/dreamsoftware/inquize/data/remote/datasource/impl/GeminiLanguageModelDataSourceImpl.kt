package com.dreamsoftware.inquize.data.remote.datasource.impl

import android.util.Log
import com.dreamsoftware.inquize.data.remote.datasource.IMultiModalLanguageModelDataSource
import com.dreamsoftware.inquize.data.remote.dto.ResolveQuestionDTO
import com.dreamsoftware.inquize.utils.urlToBitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class GeminiLanguageModelDataSourceImpl(
    private val generativeTextModel: GenerativeModel,
    private val generativeMultiModalModel: GenerativeModel,
    private val dispatcher: CoroutineDispatcher
) : IMultiModalLanguageModelDataSource {

    private companion object {
        const val USER = "user"
    }

    override suspend fun resolveQuestion(data: ResolveQuestionDTO): String = withContext(dispatcher) {
        try {
            val currentChatSession = generativeTextModel.startChat(data.history.map {
                content(it.role) { text(it.text) }
            })
            // Generate image description if image is provided
            val imageDescription = generateImageDescription(data.imageUrl)
            // Generate prompt combining the image description and the user's question
            val prompt = generatePrompt(data.question, imageDescription)
            // Send the message and return the response
           currentChatSession.sendMessage(prompt).text!!
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            throw exception
        }
    }

    /**
     * Generates a detailed description of the image located at the given [imageUrl].
     *
     * @param imageUrl The URL of the image for which a description needs to be generated.
     * @return A detailed description of the image, or `null` if an error occurs.
     */
    private suspend fun generateImageDescription(imageUrl: String): String? {
        val bitmap = imageUrl.urlToBitmap(dispatcher) ?: return null // Return null if bitmap couldn't be obtained
        return try {
            generativeMultiModalModel.generateContent(content(USER) {
                image(bitmap)
                text("Provide a detailed description of the contents of the image.")
            }).text
        } catch (exception: Exception) {
            Log.d("ATV_CHANGES", "Error generating image description: $exception")
            null // Return null if the description generation fails
        }
    }

    private fun generatePrompt(question: String, imageDescription: String?): Content =
        content(USER) {
            imageDescription?.let {
                text(
                    """
                        |You are a highly knowledgeable and perceptive assistant. A user is presenting you with something 
                        |and asking for your insights. Based on the detailed description provided below, 
                        |generate a response as if you are physically present, observing the object together with the user.
                        |
                        |Your answers should be precise, informative, and focused. Avoid engaging in dialogue or unnecessary 
                        |conversation. Instead, prioritize delivering accurate and relevant information that directly 
                        |addresses the user's query.
                        |
                        |Ensure your responses are clear and feel natural, while maintaining a professional tone. 
                        |Avoid referencing that your answers are based on a description, and respond as if you are directly 
                        |interacting with the object in real-time, providing valuable insights.
                    """.trimMargin()
                )
                text(it)
            }
            text(question)
        }

}