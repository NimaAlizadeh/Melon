package com.example.melon.repositories

import com.example.melon.api.ApiServices
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiServices: ApiServices)
{
    suspend fun getHomePosts(page: Int, limit: Int) = apiServices.getHomePosts(page, limit)
}