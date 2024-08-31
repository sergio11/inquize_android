package com.dreamsoftware.inquize.di

import android.content.Context
import com.dreamsoftware.brownie.core.IBrownieErrorMapper
import com.dreamsoftware.inquize.ui.screens.account.signin.SignInScreenSimpleErrorMapper
import com.dreamsoftware.inquize.ui.screens.account.signup.SignUpScreenSimpleErrorMapper
import com.dreamsoftware.inquize.ui.screens.home.HomeSimpleErrorMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class UiModule {

    @Provides
    @ViewModelScoped
    @SignUpScreenErrorMapper
    fun provideSignUpScreenSimpleErrorMapper(
        @ApplicationContext context: Context
    ): IBrownieErrorMapper =
        SignUpScreenSimpleErrorMapper(context = context)

    @Provides
    @ViewModelScoped
    @SignInScreenErrorMapper
    fun provideSignInScreenSimpleErrorMapper(
        @ApplicationContext context: Context
    ): IBrownieErrorMapper =
        SignInScreenSimpleErrorMapper(context = context)

    @Provides
    @ViewModelScoped
    @HomeErrorMapper
    fun provideHomeSimpleErrorMapper(
        @ApplicationContext context: Context
    ): IBrownieErrorMapper =
        HomeSimpleErrorMapper(context = context)
}
