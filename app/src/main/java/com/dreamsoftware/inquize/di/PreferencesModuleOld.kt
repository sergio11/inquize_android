package com.dreamsoftware.inquize.di

import com.dreamsoftware.inquize.data.local.preferences.PerceiveUserPreferencesManager
import com.dreamsoftware.inquize.data.local.preferences.UserPreferencesManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModuleOld {

    @Binds
    abstract fun bindPreferencesManager(impl: PerceiveUserPreferencesManager): UserPreferencesManager
}