package com.example.melon.di


import com.example.melon.BuildConfig
import com.example.melon.api.ApiServices
import com.example.melon.models.EditProfileModel
import com.example.melon.models.User
import com.example.melon.utils.Constants
import com.example.melon.utils.StoreUserData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    fun provideClient(time: Long, interceptor: HttpLoggingInterceptor, userData: StoreUserData): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(time, TimeUnit.SECONDS)
        .writeTimeout(time, TimeUnit.SECONDS)
        .connectTimeout(time, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val token: String = Constants.USER_TOKEN
            chain.proceed(chain.request().newBuilder().also {
                it.addHeader("auth-token", token)
            }.build())
        }.also {
            it.addInterceptor(interceptor)
        }
        .retryOnConnectionFailure(true)
        .build()

    @Provides
    @Singleton
    fun provideInterceptor() = HttpLoggingInterceptor().apply {
        level =
            if(BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
    }

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