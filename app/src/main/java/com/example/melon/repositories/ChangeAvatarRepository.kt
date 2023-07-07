package com.example.melon.repositories

import com.example.melon.api.ApiServices
import okhttp3.MultipartBody
import javax.inject.Inject

class ChangeAvatarRepository @Inject constructor(private val apiServices: ApiServices)
{
    suspend fun changeAvatar(token: String, avatar: MultipartBody.Part) = apiServices.changeAvatar(token, avatar)
}