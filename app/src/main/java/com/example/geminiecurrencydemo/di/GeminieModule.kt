package com.example.geminiecurrencydemo.di

import com.example.geminiecurrencydemo.network.GeminieService
import com.example.geminiecurrencydemo.repository.FirstRepository
import com.example.geminiecurrencydemo.utils.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GeminieModule {

    @Singleton
    @Provides
    fun provideRetrofit() = Retrofit.Builder().baseUrl(Constant.BaseUrl)
        .addConverterFactory(GsonConverterFactory.create()).build()

    @Singleton
    @Provides
    fun provideService(retrofit: Retrofit) = retrofit.create(GeminieService::class.java)

    @Singleton
    @Provides
    fun provideRepository(service: GeminieService) = FirstRepository(service)
}