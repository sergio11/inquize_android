package com.dreamsoftware.inquize.di

import com.dreamsoftware.inquize.framework.sound.ISoundPlayerServiceImpl
import com.dreamsoftware.inquize.domain.service.ISoundPlayerService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SoundModule {

    @Binds
    @Singleton
    abstract fun bindUISoundPlayer(impl: ISoundPlayerServiceImpl): ISoundPlayerService
}