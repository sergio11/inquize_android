package com.dreamsoftware.inquize.di

import android.content.Context
import com.dreamsoftware.inquize.domain.model.AuthRequestBO
import com.dreamsoftware.inquize.domain.model.SignUpBO
import com.dreamsoftware.inquize.domain.validation.IBusinessEntityValidator
import com.dreamsoftware.inquize.domain.validation.ISignInValidationMessagesResolver
import com.dreamsoftware.inquize.domain.validation.ISignUpValidationMessagesResolver
import com.dreamsoftware.inquize.domain.validation.impl.SignInValidatorImpl
import com.dreamsoftware.inquize.domain.validation.impl.SignUpValidatorImpl
import com.dreamsoftware.inquize.ui.validation.SignInValidationMessagesResolverImpl
import com.dreamsoftware.inquize.ui.validation.SignUpValidationMessagesResolverImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class ValidatorModule {

    @Provides
    @ViewModelScoped
    fun provideSignUpValidationMessagesResolver(
        @ApplicationContext context: Context
    ): ISignUpValidationMessagesResolver = SignUpValidationMessagesResolverImpl(context)

    @Provides
    @ViewModelScoped
    fun provideSignInValidationMessagesResolver(
        @ApplicationContext context: Context
    ): ISignInValidationMessagesResolver = SignInValidationMessagesResolverImpl(context)

    @Provides
    @ViewModelScoped
    fun provideSignUpValidator(
        messagesResolver: ISignUpValidationMessagesResolver
    ): IBusinessEntityValidator<SignUpBO> = SignUpValidatorImpl(messagesResolver)

    @Provides
    @ViewModelScoped
    fun provideSignInValidator(
        messagesResolver: ISignInValidationMessagesResolver
    ): IBusinessEntityValidator<AuthRequestBO> = SignInValidatorImpl(messagesResolver)
}