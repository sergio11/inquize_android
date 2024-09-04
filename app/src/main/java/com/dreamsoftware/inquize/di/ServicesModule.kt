package com.dreamsoftware.inquize.di

import android.content.Context
import com.dreamsoftware.inquize.domain.service.ISoundPlayerService
import com.dreamsoftware.inquize.domain.service.ITTSService
import com.dreamsoftware.inquize.domain.service.ITranscriptionService
import com.dreamsoftware.inquize.framework.sound.SoundPlayerServiceImpl
import com.dreamsoftware.inquize.framework.transcription.TranscriptionServiceImpl
import com.dreamsoftware.inquize.framework.tts.TTSServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServicesModule {

    @Provides
    @Singleton
    fun provideTranscriptionService(@ApplicationContext context: Context): ITranscriptionService = TranscriptionServiceImpl(context)

    @Provides
    @Singleton
    fun provideTextToSpeechService(@ApplicationContext context: Context): ITTSService = TTSServiceImpl(context)

    @Provides
    @Singleton
    fun provideSoundPlayerService(@ApplicationContext context: Context): ISoundPlayerService = SoundPlayerServiceImpl(context)
}