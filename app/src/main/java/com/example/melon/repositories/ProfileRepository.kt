package com.example.melon.repositories

import com.example.melon.api.ApiServices
import com.example.melon.models.AddFollowerModel
import com.example.melon.models.RemoveFollowerModel
import com.example.melon.models.UnFollowModel
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val apiServices: ApiServices) {

    suspend fun getPostsWithId(userId: String) = apiServices.getPostsWithId(userId)
    suspend fun getPostsWithToken() = apiServices.getPostsWithToken()
    suspend fun getUserDataWithId(userId: String) = apiServices.getUserDataWithId(userId)
    suspend fun getUserData() = apiServices.getUserData()
    suspend fun addFollower(body: AddFollowerModel) = apiServices.addFollower(body)
    suspend fun cancelRequest(body: RemoveFollowerModel) = apiServices.cancelRequest(body)
    suspend fun unFollow(body: UnFollowModel) = apiServices.unFollow(body)

}