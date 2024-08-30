package com.dreamsoftware.inquize.di

import com.dreamsoftware.inquize.BuildConfig
import com.dreamsoftware.inquize.data.remote.datasource.IMultiModalLanguageModelDataSource
import com.dreamsoftware.inquize.data.remote.datasource.impl.GeminiLanguageModelDataSourceImpl
import com.google.ai.client.generativeai.GenerativeModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

// Dagger module for providing Firebase-related dependencies
@Module
@InstallIn(SingletonComponent::class)
class GeminiModule {

    private companion object {
        const val GEMINI_PRO_VISION = "gemini-1.5-flash"
        const val GEMINI_PRO = "gemini-pro"
    }

    @Provides
    @Singleton
    @GenerativeTextModel
    fun provideGenerativeTextModel(): GenerativeModel = GenerativeModel(
        modelName = GEMINI_PRO,
        apiKey = BuildConfig.GOOGLE_GEMINI_API_KEY
    )

    @Provides
    @Singleton
    @GenerativeMultiModalModel
    fun provideGenerativeMultiModalModel(): GenerativeModel =  GenerativeModel(
        modelName = GEMINI_PRO_VISION,
        apiKey = BuildConfig.GOOGLE_GEMINI_API_KEY
    )

    @Provides
    @Singleton
    fun provideMultiModalLanguageModelDataSource(
        @GenerativeTextModel generativeTextModel: GenerativeModel,
        @GenerativeMultiModalModel generativeMultiModalModel: GenerativeModel,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): IMultiModalLanguageModelDataSource = GeminiLanguageModelDataSourceImpl(
        generativeTextModel,
        generativeMultiModalModel,
        dispatcher
    )
}