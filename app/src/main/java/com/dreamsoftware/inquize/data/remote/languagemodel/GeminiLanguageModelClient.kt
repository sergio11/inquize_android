package com.dreamsoftware.inquize.data.remote.languagemodel


import android.graphics.Bitmap
import com.dreamsoftware.inquize.BuildConfig
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CancellationException
import java.lang.IllegalStateException
import javax.inject.Inject

class GeminiLanguageModelClient @Inject constructor() : MultiModalLanguageModelClient {
    private val generativeTextModel = GenerativeModel(
        modelName = GeminiModels.GEMINI_PRO,
        apiKey = BuildConfig.GOOGLE_GEMINI_API_KEY
    )

    private val generativeMultiModalModel by lazy {
        GenerativeModel(
            modelName = GeminiModels.GEMINI_PRO_VISION,
            apiKey = BuildConfig.GOOGLE_GEMINI_API_KEY
        )
    }

    private var currentChatSession: Chat? = null

    override fun startNewChatSession() {
        currentChatSession = generativeTextModel.startChat()
    }

    override suspend fun sendMessage(messageContents: List<MultiModalLanguageModelClient.MessageContent>): Result<String> {
        val currentChatSession =
            currentChatSession ?: throw IllegalStateException(CHAT_SESSION_INVALID_MESSAGE)

        return try {
            val imagesInContent: List<Bitmap> = messageContents.mapNotNull {
                if (it !is MultiModalLanguageModelClient.MessageContent.Image) return@mapNotNull null
                it.bitmap
            }
            val textMessagesInContent = messageContents.mapNotNull {
                if (it !is MultiModalLanguageModelClient.MessageContent.Text) return@mapNotNull null
                it.text
            }
            var descriptionOfImages: String? = null
            // At the time of writing, gemini-pro-vision doesn't support multi-turn conversations.
            // To circumvent this, we generate a description of the images using gemini-pro-vision
            // and use it as the prompt for the gemini-pro model.
            if (imagesInContent.isNotEmpty()) {
                val content = content(GeminiRoles.USER) {
                    imagesInContent.forEach { image(it) }
                    text("Provide a detailed description of the contents of the image(s).")
                }
                descriptionOfImages = generativeMultiModalModel.generateContent(content).text ?: ""
            }
            val prompt = content(GeminiRoles.USER) {
                descriptionOfImages?.let {
                    text(
                        """
                            |You are an assistant allowing a user to point at something and ask questions about it.
                            |Based off of the following description of what the user is seeing right now,
                            |answer the question(s). Generate answers in a way that makes the 
                            |user feel as if you are with them, looking at the thing that they 
                            |are pointing to. Do not generate answers that suggest that you are 
                            |using descriptions of the images of what the user is seeing to 
                            |generate the response.
                        """.trimMargin()
                    )
                    text(it)
                }
                textMessagesInContent.forEach { text(it) }
            }
            Result.success(currentChatSession.sendMessage(prompt).text!!)
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            Result.failure(exception)
        }
    }

    override fun endChatSession() {
        currentChatSession = null
    }

    private companion object {
        object GeminiModels {
            const val GEMINI_PRO_VISION = "gemini-pro-vision"
            const val GEMINI_PRO = "gemini-pro"
        }

        object GeminiRoles {
            const val USER = "user"
            const val MODEL = "model"
        }

        const val CHAT_SESSION_INVALID_MESSAGE =
            "A chat session is not in progress. Please use startNewChat() before attempting to send new messages."
    }
}