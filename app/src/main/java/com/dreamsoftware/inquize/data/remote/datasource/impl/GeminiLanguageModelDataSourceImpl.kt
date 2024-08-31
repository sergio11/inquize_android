package com.dreamsoftware.inquize.data.remote.datasource.impl

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.dreamsoftware.inquize.data.remote.datasource.IMultiModalLanguageModelDataSource
import com.dreamsoftware.inquize.data.remote.dto.ResolveQuestionDTO
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

internal class GeminiLanguageModelDataSourceImpl(
    private val generativeTextModel: GenerativeModel,
    private val generativeMultiModalModel: GenerativeModel,
    private val dispatcher: CoroutineDispatcher
) : IMultiModalLanguageModelDataSource {

    private companion object {
        const val USER = "user"
        const val MODEL = "model"
    }

    override suspend fun resolveQuestion(data: ResolveQuestionDTO): String = withContext(dispatcher) {
        val currentChatSession = generativeTextModel.startChat(data.history.map {
            content(it.role) { text(it.text) }
        })
        Log.d("ATV_CHANGES", "GeminiLanguageModelClient sendMessage called with question and image")
        try {
            // Generate image description if image is provided
            val imageDescription = generateImageDescription(data.imageUrl)
            // Generate prompt combining the image description and the user's question
            val prompt = generatePrompt(data.question, imageDescription)
            // Send the message and return the response
           currentChatSession.sendMessage(prompt).text!!
        } catch (exception: Exception) {
            Log.d("ATV_CHANGES", "GeminiLanguageModelClient exception: $exception")
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
        val bitmap = getBitmapFromUrl(imageUrl) ?: return null // Return null if bitmap couldn't be obtained
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


    /**
     * Downloads a bitmap from the given [imageUrl].
     *
     * @param imageUrl The URL of the image to be downloaded.
     * @return The bitmap of the downloaded image, or `null` if an error occurs.
     */
    private suspend fun getBitmapFromUrl(imageUrl: String): Bitmap? = withContext(dispatcher) {
        try {
            (URL(imageUrl).openConnection() as? HttpURLConnection)?.run {
                requestMethod = "GET"
                connect()
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream.use { BitmapFactory.decodeStream(it) }
                } else {
                    null
                }
            } ?: run {
                null
            }
        } catch (e: IOException) {
            Log.e("IMAGE_LOADING", "Error fetching image: $e")
            null
        }
    }
}