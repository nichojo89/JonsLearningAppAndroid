package com.nicholssoftware.jonslearningappandroid.di

import com.nicholssoftware.jonslearningappandroid.domain.auth.Authenticator
import com.nicholssoftware.jonslearningappandroid.domain.auth.FirebaseAuthenticator
import com.nicholssoftware.jonslearningappandroid.domain.preferences.PreferencesDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthenticator(preferencesDataSource: PreferencesDataSource): Authenticator {
        return FirebaseAuthenticator(preferencesDataSource)
    }
}