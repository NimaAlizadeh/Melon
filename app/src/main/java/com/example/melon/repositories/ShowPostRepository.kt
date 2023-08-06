package com.example.melon.repositories

import com.example.melon.api.ApiServices
import javax.inject.Inject

class ShowPostRepository @Inject constructor(private val apiServices: ApiServices)
{
    suspend fun getPostsWithId(token: String, userId: String) = apiServices.getPostsWithId(token, userId)
}