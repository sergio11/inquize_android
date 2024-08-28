package com.dreamsoftware.inquize.domain.speech.transcription

/**
 * A service that listens to the user's speech and transcribes it.
 */
interface TranscriptionService {
    /**
     * Starts listening to the user's speech.
     *
     * @param transcription A callback that is called with the transcription text.
     * @param onEndOfSpeech A callback that is called when the user stops speaking.
     * @param onError A callback that is called when an error occurs.
     */
    fun startListening(
        transcription: (String) -> Unit,
        onEndOfSpeech: () -> Unit,
        onError: (Exception) -> Unit
    )

    /**
     * Stops listening to the user's speech.
     */
    fun stopListening()
}
