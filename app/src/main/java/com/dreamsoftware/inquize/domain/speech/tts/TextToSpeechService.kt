package com.dreamsoftware.inquize.domain.speech.tts

import java.lang.Exception

/**
 * A service that provides text-to-speech functionality.
 */
interface TextToSpeechService {

    /**
     * Starts speaking the given text. Throws [Exception] if any error occurs.
     *
     * @param text The text to speak.
     */
    suspend fun startSpeaking(text: String)

    /**
     * Stops speaking if the service is currently speaking.
     */
    fun stop()

    /**
     * Used to release all resources held by a particular instance. The instance
     * cannot be reused. In other words, a new instance needs to be created
     * for the [startSpeaking] method to work.
     */
    fun releaseResources()
}