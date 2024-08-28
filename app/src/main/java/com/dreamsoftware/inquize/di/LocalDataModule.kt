package com.dreamsoftware.inquize.di

import com.dreamsoftware.inquize.data.local.bitmapstore.AndroidBitmapStore
import com.dreamsoftware.inquize.data.local.bitmapstore.BitmapStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class LocalDataModule {

    @Binds
    abstract fun bindBitmapStore(impl: AndroidBitmapStore): BitmapStore
}