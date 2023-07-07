package com.example.melon.repositories

import com.example.melon.api.ApiServices
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val apiServices: ApiServices) {
    suspend fun getAllPost(token: String) = apiServices.getAllPost(token)
    suspend fun getUserData(token: String) = apiServices.getUserData(token)
}