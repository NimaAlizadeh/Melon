package com.example.melon.repositories

import com.example.melon.api.ApiServices
import com.example.melon.models.AddFollowerModel
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val apiServices: ApiServices) {
//    suspend fun getAllPost(token: String) = apiServices.getAllPost(token)
    suspend fun getPostsWithId(token: String, userId: String) = apiServices.getPostsWithId(token, userId)
    suspend fun getUserDataWithId(userId: String, token: String) = apiServices.getUserDataWithId(userId, token)
    suspend fun getUserData(token: String) = apiServices.getUserData(token)
    suspend fun addFollower(token: String, body: AddFollowerModel) = apiServices.addFollower(token, body)

}