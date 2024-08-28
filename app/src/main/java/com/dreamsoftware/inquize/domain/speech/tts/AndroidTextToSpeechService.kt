package com.dreamsoftware.inquize.domain.speech.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.IllegalArgumentException
import java.util.Locale
import javax.inject.Inject
import kotlin.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * A [TextToSpeechService] implementation that uses the Android [TextToSpeech] under the hood
 * to speak text.
 */
class AndroidTextToSpeechService @Inject constructor(
    @ApplicationContext private val context: Context
) : TextToSpeechService {

    private var textToSpeech: TextToSpeech? = null

    override suspend fun startSpeaking(text: String) {
        if (text.length > TextToSpeech.getMaxSpeechInputLength()) {
            throw IllegalArgumentException("The text length is larger than the max supported speech input length")
        }

        initializeTextToSpeech()
        textToSpeech?.language = Locale.US
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, UTTERANCE_ID)

        suspendCancellableCoroutine { continuation ->
            textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}

                override fun onError(utteranceId: String?) {
                    continuation.resumeWithException(Exception("An internal error occurred while attempting to speak"))
                }

                override fun onDone(utteranceId: String?) {
                    continuation.resume(Unit)
                }

                override fun onStop(utteranceId: String?, interrupted: Boolean) {
                    continuation.resume(Unit)
                }

            })
        }
    }

    private suspend fun initializeTextToSpeech() {
        if (textToSpeech != null) return
        suspendCancellableCoroutine { continuation ->
            textToSpeech = TextToSpeech(context) {
                if (it != TextToSpeech.ERROR) continuation.resume(Unit)
                else continuation.resumeWithException(Exception("An error occurred when initializing TextToSpeech."))
            }
        }
    }

    override fun stop() {
        textToSpeech?.run { if (isSpeaking) stop() }
    }

    override fun releaseResources() {
        textToSpeech?.shutdown()
        textToSpeech = null
    }

    private companion object {
        // utterance id is required for the UtteranceProgressListener callback to work
        private const val UTTERANCE_ID = "perceive.tttService"
    }
}