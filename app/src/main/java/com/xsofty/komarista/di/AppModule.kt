package com.xsofty.komarista.di

import com.xsofty.komarista.presentation.signin.SignInNavImpl
import com.xsofty.shared.nav.contracts.SignInNavContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesSignInNavContract(): SignInNavContract {
        return SignInNavImpl()
    }
}