package com.dreamsoftware.inquize.di

import com.dreamsoftware.inquize.framework.transcription.ITranscriptionServiceImpl
import com.dreamsoftware.inquize.domain.service.ITranscriptionService
import com.dreamsoftware.inquize.framework.tts.ITTSServiceImpl
import com.dreamsoftware.inquize.domain.service.ITTSService
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
    abstract fun bindTranscriptionService(impl: ITranscriptionServiceImpl): ITranscriptionService

    @Binds
    @Singleton
    abstract fun bindTextToSpeechService(impl: ITTSServiceImpl): ITTSService
}