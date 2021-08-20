package com.xsofty.auth.di

import com.xsofty.auth.presentation.sign_in.SignInNavImpl
import com.xsofty.shared.nav.contracts.SignInNavContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun providesSignInNavContract(): SignInNavContract {
        return SignInNavImpl()
    }
}