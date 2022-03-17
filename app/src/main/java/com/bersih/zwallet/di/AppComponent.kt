package com.bersih.zwallet.di

import android.content.Context
import com.bersih.zwallet.data.ZWalletDataSource
import com.bersih.zwallet.data.api.ZWalletApi
import com.bersih.zwallet.network.NetworkConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object AppComponent {
    @Provides
    @Singleton
    fun provideAPI(@ApplicationContext context: Context): ZWalletApi = NetworkConfig(context).buildApi()

    @Singleton
    fun provideDataSource(apiClient: ZWalletApi): ZWalletDataSource = ZWalletDataSource(apiClient)
}