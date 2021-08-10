package com.xsofty.shared.di

import androidx.annotation.Nullable
import com.xsofty.shared.BuildConfig
import com.xsofty.shared.network.RequestInterceptor
import com.xsofty.shared.storage.AppPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    private const val SERVER_URL = "https://europe-west3-komar-app.cloudfunctions.net/api/"

    @Singleton
    @Provides
    @OkHttpRequestInterceptor
    fun provideRequestInterceptor(
        appPreferences: AppPreferences
    ): Interceptor = RequestInterceptor(appPreferences)

    @Singleton
    @Provides
    @OkHttpLoggingInterceptor
    @Nullable
    fun provideLoggingInterceptor(): Interceptor? =
        if (BuildConfig.DEBUG)
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            } else null

    @Singleton
    @Provides
    fun provideConverterFactory(): MoshiConverterFactory =
        MoshiConverterFactory.create().asLenient()

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @OkHttpLoggingInterceptor loggingInterceptor: Interceptor?,
        @OkHttpRequestInterceptor requestInterceptor: Interceptor,
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(requestInterceptor)
            .apply {
                loggingInterceptor?.let { interceptor -> addInterceptor(interceptor) }
            }
            .build()

    @Singleton
    @Provides
    fun provideRetrofitClient(
        okHttpClient: OkHttpClient,
        converterFactory: MoshiConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .baseUrl(SERVER_URL)
            .client(okHttpClient)
            .build()

    @Qualifier
    annotation class OkHttpRequestInterceptor

    @Qualifier
    annotation class OkHttpLoggingInterceptor
}