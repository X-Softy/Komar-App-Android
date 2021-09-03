package com.xsofty.shared.di

import android.content.Context
import com.xsofty.shared.theme.ThemeManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ThemeModule {

    @Singleton
    @Provides
    fun provideThemeManager(@ApplicationContext appContext: Context): ThemeManager {
        return ThemeManager(appContext)
    }
}