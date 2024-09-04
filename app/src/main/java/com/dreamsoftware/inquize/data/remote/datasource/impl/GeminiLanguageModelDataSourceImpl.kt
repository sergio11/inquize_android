package com.dreamsoftware.inquize.data.remote.datasource.impl

import com.dreamsoftware.inquize.data.remote.datasource.IMultiModalLanguageModelDataSource
import com.dreamsoftware.inquize.data.remote.dto.ResolveQuestionDTO
import com.dreamsoftware.inquize.data.remote.exception.GenerateImageDescriptionRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.ResolveQuestionFromContextRemoteDataException
import com.dreamsoftware.inquize.utils.urlToBitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
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

    @Throws(ResolveQuestionFromContextRemoteDataException::class)
    override suspend fun resolveQuestionFromContext(data: ResolveQuestionDTO): String = withContext(dispatcher) {
        try {
            val currentChatSession = generativeTextModel.startChat(data.history.map {
                content(it.role) { text(it.text) }
            })
            // Generate prompt combining the context and the user's question
            val prompt = generatePrompt(data.question, data.context)
            // Send the message and return the response
           currentChatSession.sendMessage(prompt).text ?: throw IllegalStateException("Answer couldn't be obtained")
        } catch (ex: Exception) {
            throw ResolveQuestionFromContextRemoteDataException("An error occurred when trying to resolver user question", ex)
        }
    }

    /**
     * Generates a detailed description of the image located at the given [imageUrl].
     *
     * @param imageUrl The URL of the image for which a description needs to be generated.
     * @return A detailed description of the image, or `null` if an error occurs.
     */
    @Throws(GenerateImageDescriptionRemoteDataException::class)
    override suspend fun generateImageDescription(imageUrl: String): String = withContext(dispatcher) {
        try {
            val bitmap = imageUrl.urlToBitmap(dispatcher) ?: throw IllegalStateException("bitmap couldn't be obtained")
            generativeMultiModalModel.generateContent(content(USER) {
                image(bitmap)
                text("Provide a detailed description of the contents of the image.")
            }).text  ?: throw IllegalStateException("Description couldn't be obtained")
        } catch (ex: Exception) {
            throw GenerateImageDescriptionRemoteDataException("An error occurred when trying to generate the image description", ex)
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