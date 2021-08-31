package com.xsofty.rooms.di

import com.xsofty.rooms.data.network.api.RoomDetailsApi
import com.xsofty.rooms.data.repository.RoomDetailsRepositoryImpl
import com.xsofty.rooms.domain.repository.RoomDetailsRepository
import com.xsofty.rooms.presentation.details.RoomDetailsNavImpl
import com.xsofty.shared.nav.contracts.RoomDetailsNavContract
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDetailsModule {

    @Singleton
    @Provides
    fun provideRoomDetailsApi(retrofit: Retrofit): RoomDetailsApi {
        return retrofit.create(RoomDetailsApi::class.java)
    }

    @Singleton
    @Provides
    fun providesRoomDetailsNavContract(): RoomDetailsNavContract {
        return RoomDetailsNavImpl()
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RoomDetailsDataModule {

    @Binds
    abstract fun bindsRoomDetailsRepository(
        repository: RoomDetailsRepositoryImpl
    ): RoomDetailsRepository
}