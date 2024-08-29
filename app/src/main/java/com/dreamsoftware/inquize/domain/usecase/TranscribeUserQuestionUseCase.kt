package com.dreamsoftware.inquize.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCase
import com.dreamsoftware.inquize.domain.service.ITranscriptionService
import kotlinx.coroutines.CompletableDeferred

class TranscribeUserQuestionUseCase(
    private val transcriptionService: ITranscriptionService
) : BrownieUseCase<String>() {

    override suspend fun onExecuted(): String =
        listenForTranscription().takeUnless { it.isNullOrBlank() }
            ?: throw IllegalStateException("Transcription is null or empty")

    /**
     * Starts listening for speech transcription.
     * Uses CompletableDeferred to wait until the end of speech or an error occurs.
     */
    private suspend fun listenForTranscription(): String? = CompletableDeferred<String?>().run {
        var lastTranscription: String? = null // Variable to store the latest transcription
        // Start the transcription service and listen for speech
        with(transcriptionService) {
            startListening(
                transcription = { transcription ->
                    lastTranscription = transcription // Update the latest transcription
                },
                onEndOfSpeech = {
                    // Complete with the latest transcription
                    complete(lastTranscription).also {
                        stopListening()
                    }
                },
                onError = { error ->
                    // Propagate error if occurs
                    completeExceptionally(error).also {
                        stopListening()
                    }
                }
            )
        }
        // Wait until either transcription is completed or an error occurs
        await()
    }
}