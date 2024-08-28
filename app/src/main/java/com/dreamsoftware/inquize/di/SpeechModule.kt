package com.dreamsoftware.inquize.di

import com.dreamsoftware.inquize.domain.speech.transcription.AndroidTranscriptionService
import com.dreamsoftware.inquize.domain.speech.transcription.TranscriptionService
import com.dreamsoftware.inquize.domain.speech.tts.AndroidTextToSpeechService
import com.dreamsoftware.inquize.domain.speech.tts.TextToSpeechService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SpeechModule {

    @Binds
    @Singleton
    abstract fun bindTranscriptionService(impl: AndroidTranscriptionService): TranscriptionService

    @Binds
    @Singleton
    abstract fun bindTextToSpeechService(impl: AndroidTextToSpeechService): TextToSpeechService
}