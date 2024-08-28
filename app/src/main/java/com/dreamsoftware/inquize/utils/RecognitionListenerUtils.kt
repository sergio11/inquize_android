package com.dreamsoftware.inquize.utils

import android.os.Bundle
import android.speech.RecognitionListener

/**
 * Builds a recognition listener that handles various speech recognition events.
 *
 * Important points to note about the [RecognitionListener]
 *
 * - The [onPartialResults] callback, as the name implies, only gives partial results.
 *   This means that the last word / last couple of words has /have a high probability of not
 *   getting transcribed (This was also experienced when using this callback exclusively while
 *   manually testing this app).Hence, please also use [onResults] to get the full transcription
 *   at the end.
 *
 *  - Don't use [onEndOfSpeech] callback to stop listening to the user / stopping transcription.
 *    It only indicates the "end of speech". It doesn't really imply that the entire speech
 *    has be transcribed. Always use the [onResults] callback to get the full transcript.
 *
 * @param onReadyForSpeech Called when the recognition is ready to start listening for speech.
 * @param onBeginningOfSpeech Called when the user starts speaking.
 * @param onRmsChanged Called when the RMS (root mean square) dB value of the audio stream changes.
 * @param onBufferReceived Called when a new audio buffer is received.
 * @param onEndOfSpeech Called when the user stops speaking.
 * @param onError Called when an error occurs during speech recognition.
 * @param onResults Called when speech recognition results are available.
 * @param onPartialResults Called when partial speech recognition results are available.
 * @param onSegmentResults Called when segment-level speech recognition results are available.
 * @param onEndOfSegmentedSession Called when a segmented session ends.
 * @param onLanguageDetection Called when the language of the speech is detected.
 * @param onEvent Called when a generic event occurs.
 * @return A recognition listener that can be used to handle speech recognition events.
 */
fun createRecognitionListener(
    onReadyForSpeech: ((params: Bundle?) -> Unit)? = null,
    onBeginningOfSpeech: (() -> Unit)? = null,
    onRmsChanged: ((rmsdB: Float) -> Unit)? = null,
    onBufferReceived: ((buffer: ByteArray?) -> Unit)? = null,
    onEndOfSpeech: (() -> Unit)? = null,
    onError: ((error: Int) -> Unit)? = null,
    onResults: ((results: Bundle?) -> Unit)? = null,
    onPartialResults: ((partialResults: Bundle?) -> Unit)? = null,
    onSegmentResults: ((segmentResults: Bundle?) -> Unit)? = null,
    onEndOfSegmentedSession: (() -> Unit)? = null,
    onLanguageDetection: ((results: Bundle?) -> Unit)? = null,
    onEvent: ((eventType: Int, params: Bundle?) -> Unit)? = null
): RecognitionListener {
    return object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            onReadyForSpeech?.invoke(params)
        }

        override fun onBeginningOfSpeech() {
            onBeginningOfSpeech?.invoke()
        }

        override fun onRmsChanged(rmsdB: Float) {
            onRmsChanged?.invoke(rmsdB)
        }

        override fun onBufferReceived(buffer: ByteArray?) {
            onBufferReceived?.invoke(buffer)
        }

        override fun onEndOfSpeech() {
            onEndOfSpeech?.invoke()
        }

        override fun onError(error: Int) {
            onError?.invoke(error)
        }

        override fun onResults(results: Bundle?) {
            onResults?.invoke(results)
        }

        override fun onPartialResults(partialResults: Bundle?) {
            onPartialResults?.invoke(partialResults)
        }

        override fun onSegmentResults(segmentResults: Bundle) {
            onSegmentResults?.invoke(segmentResults)
        }

        override fun onEndOfSegmentedSession() {
            onEndOfSegmentedSession?.invoke()
        }

        override fun onLanguageDetection(results: Bundle) {
            onLanguageDetection?.invoke(results)
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
            onEvent?.invoke(eventType, params)
        }
    }
}