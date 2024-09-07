package com.dreamsoftware.inquize.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dreamsoftware.inquize.data.local.preferences.datasource.IPreferencesDataSource
import com.dreamsoftware.inquize.data.local.preferences.datasource.impl.PreferencesDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PreferencesModule {

    private companion object {
        @JvmStatic
        val DATA_STORE_NAME = "DATA_STORE"
    }

    /**
     * Provide Data Store
     * @param context
     */
    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        preferencesDataStore(name = DATA_STORE_NAME).getValue(context, String::javaClass)

    /**
     * Provide Preference DataSource
     * @param dataStore
     * @param dispatcher
     */
    @Singleton
    @Provides
    fun providePreferenceDataSource(
        dataStore: DataStore<Preferences>,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): IPreferencesDataSource =
        PreferencesDataSourceImpl(dataStore, dispatcher)
}