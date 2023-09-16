package com.example.melon.di

import androidx.recyclerview.widget.PagerSnapHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OthersModule {

    @Provides
    @Singleton
    fun provideSnapHelper() = PagerSnapHelper()
}