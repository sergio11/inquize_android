package com.dreamsoftware.inquize.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.dreamsoftware.inquize.data.local.preferences.PerceiveUserPreferencesManager
import com.dreamsoftware.inquize.data.local.preferences.UserPreferencesManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {

    @Binds
    abstract fun bindPreferencesManager(impl: PerceiveUserPreferencesManager): UserPreferencesManager

    companion object {
        @Provides
        @Singleton
        fun providePreferencesDataStore(
            @ApplicationContext context: Context
        ): DataStore<Preferences> = PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(USER_PREFERENCES_STORE_NAME)
        }
    }
}

private const val USER_PREFERENCES_STORE_NAME = "perceive_user_preferences"