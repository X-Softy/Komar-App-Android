package com.xsofty.filter.di

import com.xsofty.filter.data.datasource.remote.HelloWorldApi
import com.xsofty.filter.data.datasource.remote.HelloWorldRemoteDataSource
import com.xsofty.filter.data.datasource.remote.HelloWorldRemoteDataSourceImpl
import com.xsofty.filter.data.repository.HelloWorldRepositoryImpl
import com.xsofty.filter.domain.repository.HelloWorldRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HelloWorldApiModule {

    @Singleton
    @Provides
    fun provideHelloWorldApi(retrofit: Retrofit): HelloWorldApi {
        return retrofit.create(HelloWorldApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class HelloWorldDataModule {

    @Singleton
    @Binds
    abstract fun bindsHelloWorldRemoteDataSource(
        remoteDataSource: HelloWorldRemoteDataSourceImpl
    ): HelloWorldRemoteDataSource

    @Binds
    abstract fun bindsHelloWorldRepository(
        repository: HelloWorldRepositoryImpl
    ): HelloWorldRepository
}