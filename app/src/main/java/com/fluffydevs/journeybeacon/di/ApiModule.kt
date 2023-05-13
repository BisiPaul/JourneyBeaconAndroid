package com.fluffydevs.journeybeacon.di

import com.fluffydevs.journeybeacon.domain.api.ApiClient
import com.fluffydevs.journeybeacon.domain.api.ServiceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    @Singleton
    fun provideApiClient(): ApiClient = ApiClient()

    @Provides
    @Singleton
    fun provideRetrofit(apiClient: ApiClient): Retrofit = apiClient.getClient()

    @Provides
    @Singleton
    fun provideServiceApi(retrofit: Retrofit): ServiceApi = retrofit.create(ServiceApi::class.java)
}