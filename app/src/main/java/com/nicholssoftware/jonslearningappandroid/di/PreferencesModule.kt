package com.nicholssoftware.jonslearningappandroid.di

import android.content.Context
import com.nicholssoftware.jonslearningappandroid.data.cache.preferences.PreferencesDataSourceImpl
import com.nicholssoftware.jonslearningappandroid.domain.preferences.PreferencesDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun providePreferencesDataSource(
        @ApplicationContext context: Context
    ): PreferencesDataSource {
        return PreferencesDataSourceImpl(context)
    }
}