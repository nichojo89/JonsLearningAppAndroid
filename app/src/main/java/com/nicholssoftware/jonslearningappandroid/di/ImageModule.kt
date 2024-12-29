package com.nicholssoftware.jonslearningappandroid.di

import android.content.Context
import com.nicholssoftware.jonslearningappandroid.data.image.repository.ImageRepository
import com.nicholssoftware.jonslearningappandroid.data.image.repository.ImageRepositoryImpl
import com.nicholssoftware.jonslearningappandroid.data.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ImageModule {
    @Provides
    @Singleton
    fun provideImageRepository(apiService: ApiService, @ApplicationContext context: Context): ImageRepository {
        return ImageRepositoryImpl(context,apiService)
    }
}