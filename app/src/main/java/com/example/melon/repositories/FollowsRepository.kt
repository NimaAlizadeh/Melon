package com.example.melon.repositories

import com.example.melon.api.ApiServices
import com.example.melon.models.AddFollowerModel
import com.example.melon.models.RemoveFollowModel
import com.example.melon.models.RemoveFollowerModel
import com.example.melon.models.UnFollowModel
import javax.inject.Inject

class FollowsRepository @Inject constructor(private val apiServices: ApiServices) {

    suspend fun addFollower(body: AddFollowerModel) = apiServices.addFollower(body)
    suspend fun cancelRequest(body: RemoveFollowerModel) = apiServices.cancelRequest(body)
    suspend fun unFollow(body: UnFollowModel) = apiServices.unFollow(body)
    suspend fun removeFollower(body: RemoveFollowModel) = apiServices.removeFollower(body)

}