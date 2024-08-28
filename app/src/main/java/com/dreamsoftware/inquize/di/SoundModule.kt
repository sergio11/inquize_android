package com.dreamsoftware.inquize.di

import com.dreamsoftware.inquize.domain.sound.AndroidUISoundPlayer
import com.dreamsoftware.inquize.domain.sound.UISoundPlayer
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
    abstract fun bindUISoundPlayer(impl: AndroidUISoundPlayer): UISoundPlayer
}