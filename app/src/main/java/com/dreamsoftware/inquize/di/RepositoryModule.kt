package com.dreamsoftware.inquize.di

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.inquize.data.local.preferences.datasource.IPreferencesDataSource
import com.dreamsoftware.inquize.data.remote.datasource.IAuthRemoteDataSource
import com.dreamsoftware.inquize.data.remote.datasource.IImageDataSource
import com.dreamsoftware.inquize.data.remote.datasource.IInquizeDataSource
import com.dreamsoftware.inquize.data.remote.datasource.IMultiModalLanguageModelDataSource
import com.dreamsoftware.inquize.data.remote.dto.AuthUserDTO
import com.dreamsoftware.inquize.data.remote.dto.InquizeDTO
import com.dreamsoftware.inquize.data.remote.dto.ResolveQuestionDTO
import com.dreamsoftware.inquize.data.remote.dto.SaveInquizeDTO
import com.dreamsoftware.inquize.data.repository.impl.IMultiModalLanguageModelRepositoryImpl
import com.dreamsoftware.inquize.data.repository.impl.InquizeRepositoryImpl
import com.dreamsoftware.inquize.data.repository.impl.PreferenceRepositoryImpl
import com.dreamsoftware.inquize.data.repository.impl.UserRepositoryImpl
import com.dreamsoftware.inquize.data.repository.mapper.AuthUserMapper
import com.dreamsoftware.inquize.data.repository.mapper.InquizeMapper
import com.dreamsoftware.inquize.data.repository.mapper.ResolveQuestionMapper
import com.dreamsoftware.inquize.data.repository.mapper.SaveInquizeMapper
import com.dreamsoftware.inquize.domain.model.AuthUserBO
import com.dreamsoftware.inquize.domain.model.InquizeBO
import com.dreamsoftware.inquize.domain.model.ResolveQuestionBO
import com.dreamsoftware.inquize.domain.model.SaveInquizeBO
import com.dreamsoftware.inquize.domain.repository.IInquizeRepository
import com.dreamsoftware.inquize.domain.repository.IMultiModalLanguageModelRepository
import com.dreamsoftware.inquize.domain.repository.IPreferenceRepository
import com.dreamsoftware.inquize.domain.repository.IUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthUserMapper(): IBrownieOneSideMapper<AuthUserDTO, AuthUserBO> = AuthUserMapper()

    @Provides
    @Singleton
    fun provideInquizeMapper(): IBrownieOneSideMapper<InquizeDTO, InquizeBO> = InquizeMapper()

    @Provides
    @Singleton
    fun provideSaveInquizeMapper(): IBrownieOneSideMapper<SaveInquizeBO, SaveInquizeDTO> = SaveInquizeMapper()

    @Provides
    @Singleton
    fun provideResolveQuestionMapper(): IBrownieOneSideMapper<ResolveQuestionBO, ResolveQuestionDTO> = ResolveQuestionMapper()

    @Provides
    @Singleton
    fun provideUserRepository(
        authDataSource: IAuthRemoteDataSource,
        authUserMapper: IBrownieOneSideMapper<AuthUserDTO, AuthUserBO>,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): IUserRepository =
        UserRepositoryImpl(
            authDataSource,
            authUserMapper,
            dispatcher
        )

    @Provides
    @Singleton
    fun providePreferenceRepository(
        preferenceDataSource: IPreferencesDataSource,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): IPreferenceRepository =
        PreferenceRepositoryImpl(
            preferenceDataSource,
            dispatcher
        )

    @Provides
    @Singleton
    fun provideInquizeRepository(
        inquizeDataSource: IInquizeDataSource,
        imageDataSource: IImageDataSource,
        saveInquizeMapper: IBrownieOneSideMapper<SaveInquizeBO, SaveInquizeDTO>,
        inquizeMapper: IBrownieOneSideMapper<InquizeDTO, InquizeBO>,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): IInquizeRepository =
        InquizeRepositoryImpl(
            inquizeDataSource,
            imageDataSource,
            saveInquizeMapper,
            inquizeMapper,
            dispatcher
        )

    @Provides
    @Singleton
    fun provideMultiModalLanguageModelRepository(
        multiModalLanguageModelDataSource: IMultiModalLanguageModelDataSource,
        resolveQuestionMapper: IBrownieOneSideMapper<ResolveQuestionBO, ResolveQuestionDTO>,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): IMultiModalLanguageModelRepository =
        IMultiModalLanguageModelRepositoryImpl(
            multiModalLanguageModelDataSource,
            resolveQuestionMapper,
            dispatcher
        )
}
