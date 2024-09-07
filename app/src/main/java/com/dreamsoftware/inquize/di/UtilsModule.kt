package com.dreamsoftware.inquize.di

import android.content.Context
import com.dreamsoftware.brownie.utils.BrownieEventBus
import com.dreamsoftware.brownie.utils.network.BrownieNetworkConnectivityCallback
import com.dreamsoftware.brownie.utils.network.BrownieNetworkConnectivityMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {

    @Singleton
    @Provides
    fun provideAppEventBus() = BrownieEventBus()

    @Singleton
    @Provides
    fun provideNetworkCallback(appEventBus: BrownieEventBus) = BrownieNetworkConnectivityCallback(appEventBus)

    @Singleton
    @Provides
    fun provideNetworkConnectivityMonitor(
        @ApplicationContext context: Context,
        networkConnectivityCallback: BrownieNetworkConnectivityCallback
    ) = BrownieNetworkConnectivityMonitor(context, networkConnectivityCallback)
}