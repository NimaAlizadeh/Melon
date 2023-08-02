package com.example.melon.repositories

import com.example.melon.api.ApiServices
import javax.inject.Inject

class ShowPostRepository @Inject constructor(private val apiServices: ApiServices)
{
    suspend fun getUserDataWithId(userId: String, token: String) = apiServices.getUserDataWithId(userId, token)
}