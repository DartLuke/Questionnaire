package com.danielpasser.questionnaire.di

import com.danielpasser.questionnaire.api.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {
    // private val BASE_URL="http://192.168.1.106:3000"
     //private val BASE_URL ="http://localhost:3000/"
    private val BASE_URL="http://10.0.2.2:3000"
    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient) =
        Retrofit.Builder().baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create())

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val interceptorLogger = HttpLoggingInterceptor()
        interceptorLogger.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(interceptorLogger).build()
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit.Builder): Api = retrofit.build().create(Api::class.java)
}