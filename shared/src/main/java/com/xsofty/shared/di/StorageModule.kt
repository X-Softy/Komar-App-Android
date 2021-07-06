package com.xsofty.shared.di

import android.content.Context
import android.content.SharedPreferences
import com.xsofty.shared.storage.AppPreferences
import com.xsofty.shared.storage.AppPreferencesImpl
import com.xsofty.shared.storage.DataCleaner
import com.xsofty.shared.storage.PreferencesCleaner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("komarista", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideAppPreferences(sharedPreferences: SharedPreferences): AppPreferences =
        AppPreferencesImpl(sharedPreferences)

    @Singleton
    @Provides
    @IntoSet
    fun providePreferencesCleaner(sharedPreferences: SharedPreferences): DataCleaner.Cleanable =
        PreferencesCleaner(sharedPreferences)
}