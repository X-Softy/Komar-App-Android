package com.xsofty.categories.di

import com.xsofty.categories.data.network.api.CategoriesApi
import com.xsofty.categories.data.repository.CategoriesRepositoryImpl
import com.xsofty.categories.domain.repository.CategoriesRepository
import com.xsofty.categories.presentation.CategoriesNavImpl
import com.xsofty.shared.nav.contracts.CategoriesNavContract
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CategoriesModule {

    @Singleton
    @Provides
    fun provideCategoriesApi(retrofit: Retrofit): CategoriesApi {
        return retrofit.create(CategoriesApi::class.java)
    }

    @Singleton
    @Provides
    fun providesCategoriesNavContract(): CategoriesNavContract {
        return CategoriesNavImpl()
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CategoriesDataModule {

    @Binds
    abstract fun bindsCategoriesRepository(
        repository: CategoriesRepositoryImpl
    ): CategoriesRepository
}