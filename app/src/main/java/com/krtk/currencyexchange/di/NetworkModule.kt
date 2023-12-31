package com.krtk.currencyexchange.di

import com.krtk.currencyexchange.BuildConfig
import com.krtk.currencyexchange.dataLayer.remote.ApiService
import com.krtk.currencyexchange.dataLayer.remote.interceptors.QueryInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

//Use dependency injection to provide dependencies to the classes in the presentation layer.
@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    @Singleton
    @Named("loggingInterceptor")
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    @Named("queryInterceptor")
    fun provideQueryInterceptor(): QueryInterceptor {
        return QueryInterceptor()
    }

    @Singleton
    @Provides
    fun provideOkhttpClient(
        @Named("loggingInterceptor") loggingInterceptor: HttpLoggingInterceptor,
        @Named("queryInterceptor") queryInterceptor: QueryInterceptor

    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(loggingInterceptor)
            }
            addInterceptor(queryInterceptor)
            connectTimeout(30, TimeUnit.SECONDS)
        }.build()
    }

    @Provides
    @Singleton
    fun providesMoshi(): Moshi = Moshi
        .Builder()
        .run {
            add(KotlinJsonAdapterFactory())
            build()
        }

    @Provides
    @Singleton
    fun providesApiService(moshi: Moshi, okHttpClient: OkHttpClient): ApiService = Retrofit
        .Builder()
        .run {
            baseUrl(ApiService.BASE_URL)
            addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(okHttpClient)
            build()
        }.create(ApiService::class.java)

}