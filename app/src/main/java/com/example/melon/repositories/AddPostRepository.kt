package com.example.melon.repositories

import com.example.melon.api.ApiServices
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AddPostRepository @Inject constructor(private val apiServices: ApiServices)
{
    suspend fun addPost(time: RequestBody, description: RequestBody, images: List<MultipartBody.Part>) = apiServices.addPost(time,description, images)
}