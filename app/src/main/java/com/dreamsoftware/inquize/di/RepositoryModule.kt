package com.dreamsoftware.inquize.di

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.inquize.data.local.preferences.datasource.IPreferencesDataSource
import com.dreamsoftware.inquize.data.remote.datasource.IAuthRemoteDataSource
import com.dreamsoftware.inquize.data.remote.dto.AuthUserDTO
import com.dreamsoftware.inquize.data.repository.impl.PreferenceRepositoryImpl
import com.dreamsoftware.inquize.data.repository.impl.UserRepositoryImpl
import com.dreamsoftware.inquize.data.repository.mapper.AuthUserMapper
import com.dreamsoftware.inquize.domain.model.AuthUserBO
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
}
