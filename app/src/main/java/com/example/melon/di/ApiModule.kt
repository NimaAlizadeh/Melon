package com.example.melon.di


import com.example.melon.api.ApiServices
import com.example.melon.models.EditProfileModel
import com.example.melon.models.GetUserResponse
import com.example.melon.models.User
import com.example.melon.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule
{
    @Provides
    @Singleton
    fun provideBaseUrl(): String = Constants.BASE_URL


    @Provides
    @Singleton
    fun provideConnectionTime(): Long = Constants.CLIENT_TIMEOUT

    @Provides
    @Singleton
    fun provideClient(time: Long): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(time, TimeUnit.SECONDS)
        .writeTimeout(time, TimeUnit.SECONDS)
        .connectTimeout(time, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, client: OkHttpClient, gson: Gson): ApiServices =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServices::class.java)

    @Provides
    @Singleton
    fun provideEditProfileModel() = EditProfileModel()

    @Provides
    @Singleton
    fun provideUserResponse() = User()
}