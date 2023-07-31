package com.example.melon.repositories

import com.example.melon.api.ApiServices
import com.example.melon.models.AcceptOrRejectModel
import javax.inject.Inject

class NotificationRepository @Inject constructor(private val apiServices: ApiServices)
{
    suspend fun getRequests(token: String) = apiServices.getRequests(token)
    suspend fun acceptFollowRequest(token: String, body: AcceptOrRejectModel) = apiServices.acceptFollow(token,body)
    suspend fun rejectFollowRequest(token: String, body: AcceptOrRejectModel) = apiServices.rejectFollow(token,body)
}