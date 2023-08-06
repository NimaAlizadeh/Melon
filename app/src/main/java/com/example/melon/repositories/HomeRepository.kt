package com.example.melon.repositories

import com.example.melon.api.ApiServices
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiServices: ApiServices)
{
//    suspend fun getAllPostHome() = apiServices.getAllPostHome()
    suspend fun getUserData(token: String) = apiServices.getUserData(token)
}