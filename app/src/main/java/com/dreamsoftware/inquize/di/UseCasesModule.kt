package com.dreamsoftware.inquize.di

import com.dreamsoftware.inquize.domain.model.AuthRequestBO
import com.dreamsoftware.inquize.domain.model.SignUpBO
import com.dreamsoftware.inquize.domain.repository.IInquizeRepository
import com.dreamsoftware.inquize.domain.repository.IMultiModalLanguageModelRepository
import com.dreamsoftware.inquize.domain.repository.IPreferenceRepository
import com.dreamsoftware.inquize.domain.repository.IUserRepository
import com.dreamsoftware.inquize.domain.service.ITranscriptionService
import com.dreamsoftware.inquize.domain.usecase.CreateInquizeUseCase
import com.dreamsoftware.inquize.domain.usecase.DeleteInquizeByIdUseCase
import com.dreamsoftware.inquize.domain.usecase.TranscribeUserQuestionUseCase
import com.dreamsoftware.inquize.domain.usecase.EndUserSpeechCaptureUseCase
import com.dreamsoftware.inquize.domain.usecase.GetAllInquizeByUserUseCase
import com.dreamsoftware.inquize.domain.usecase.GetAuthenticateUserDetailUseCase
import com.dreamsoftware.inquize.domain.usecase.SignInUseCase
import com.dreamsoftware.inquize.domain.usecase.SignOffUseCase
import com.dreamsoftware.inquize.domain.usecase.SignUpUseCase
import com.dreamsoftware.inquize.domain.usecase.VerifyUserSessionUseCase
import com.dreamsoftware.inquize.domain.validation.IBusinessEntityValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class UseCasesModule {

    @Provides
    @ViewModelScoped
    fun provideVerifyUserSessionUseCase(
        userRepository: IUserRepository
    ): VerifyUserSessionUseCase =
        VerifyUserSessionUseCase(userRepository)

    @Provides
    @ViewModelScoped
    fun provideTranscribeUserQuestionUseCase(
        transcriptionService: ITranscriptionService
    ): TranscribeUserQuestionUseCase =
        TranscribeUserQuestionUseCase(transcriptionService)

    @Provides
    @ViewModelScoped
    fun provideEndUserSpeechCaptureUseCase(
        transcriptionService: ITranscriptionService
    ): EndUserSpeechCaptureUseCase =
        EndUserSpeechCaptureUseCase(transcriptionService)

    @Provides
    @ViewModelScoped
    fun provideSignInUseCase(
        userRepository: IUserRepository,
        preferenceRepository: IPreferenceRepository,
        validator: IBusinessEntityValidator<AuthRequestBO>
    ): SignInUseCase =
        SignInUseCase(
            userRepository = userRepository,
            preferenceRepository = preferenceRepository,
            validator = validator
        )

    @Provides
    @ViewModelScoped
    fun provideSignUpUseCase(
        preferenceRepository: IPreferenceRepository,
        userRepository: IUserRepository,
        validator: IBusinessEntityValidator<SignUpBO>
    ): SignUpUseCase =
        SignUpUseCase(
            userRepository = userRepository,
            preferenceRepository = preferenceRepository,
            validator = validator
        )

    @Provides
    @ViewModelScoped
    fun provideSignOffUseCase(
        userRepository: IUserRepository,
        preferenceRepository: IPreferenceRepository,
    ): SignOffUseCase =
        SignOffUseCase(
            userRepository = userRepository,
            preferenceRepository = preferenceRepository
        )

    @Provides
    @ViewModelScoped
    fun provideCreateInquizeUseCase(
        userRepository: IUserRepository,
        inquizeRepository: IInquizeRepository,
        multiModalLanguageModelRepository: IMultiModalLanguageModelRepository
    ): CreateInquizeUseCase =
        CreateInquizeUseCase(
            userRepository = userRepository,
            inquizeRepository = inquizeRepository,
            multiModalLanguageModelRepository = multiModalLanguageModelRepository
        )

    @Provides
    @ViewModelScoped
    fun provideDeleteInquizeByIdUseCase(
        userRepository: IUserRepository,
        inquizeRepository: IInquizeRepository
    ): DeleteInquizeByIdUseCase =
        DeleteInquizeByIdUseCase(
            userRepository = userRepository,
            inquizeRepository = inquizeRepository
        )

    @Provides
    @ViewModelScoped
    fun provideGetAllInquizeByUserUseCase(
        userRepository: IUserRepository,
        inquizeRepository: IInquizeRepository
    ): GetAllInquizeByUserUseCase =
        GetAllInquizeByUserUseCase(
            userRepository = userRepository,
            inquizeRepository = inquizeRepository
        )

    @Provides
    @ViewModelScoped
    fun provideGetAuthenticateUserDetailUseCase(
        userRepository: IUserRepository
    ): GetAuthenticateUserDetailUseCase =
        GetAuthenticateUserDetailUseCase(
            userRepository = userRepository,
        )
}
