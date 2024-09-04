package com.dreamsoftware.inquize.framework.transcription

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.dreamsoftware.inquize.domain.service.ITranscriptionService
import com.dreamsoftware.inquize.utils.createRecognitionListener
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * A concrete impl of [ITranscriptionService] that uses [SpeechRecognizer].
 */
class TranscriptionServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ITranscriptionService {

    private val speechRecognizer by lazy {
        SpeechRecognizer.createSpeechRecognizer(context)
    }
    private val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    }

    override fun startListening(
        transcription: (String) -> Unit,
        onEndOfSpeech: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val listener = createRecognitionListener(
            onEndOfSpeech = { /* See docs on why this is not being used */ },
            onError = { onError(Exception("An error occurred when transcribing.")) },
            onPartialResults = { partialResultsBundle ->
                val transcript = partialResultsBundle
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.first() ?: return@createRecognitionListener
                transcription(transcript)
            },
            onResults = { resultsBundle ->
                // See createRecognitionListener() docs to understand why this is
                // also used in addition to onPartialResults.
                val transcript = resultsBundle
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.first() ?: return@createRecognitionListener
                transcription(transcript)
                onEndOfSpeech()
            }
        )
        stopListening()
        with(speechRecognizer) {
            setRecognitionListener(listener)
            startListening(speechRecognizerIntent)
        }
    }

    override fun stopListening() {
        with(speechRecognizer) {
            stopListening()
            destroy()
        }
    }
}