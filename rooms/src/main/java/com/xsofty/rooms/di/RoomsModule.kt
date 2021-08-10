package com.xsofty.rooms.di

import com.xsofty.rooms.data.network.RoomsApi
import com.xsofty.rooms.data.repository.RoomsRepositoryImpl
import com.xsofty.rooms.domain.repository.RoomsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomsModule {

    @Singleton
    @Provides
    fun provideRoomsApi(retrofit: Retrofit): RoomsApi {
        return retrofit.create(RoomsApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RoomsDataModule {

    @Binds
    abstract fun bindsRoomsRepository(
        repository: RoomsRepositoryImpl
    ): RoomsRepository
}