package com.example.melon.repositories

import com.example.melon.api.ApiServices
import com.example.melon.models.AcceptOrRejectModel
import javax.inject.Inject

class NotificationRepository @Inject constructor(private val apiServices: ApiServices)
{
    suspend fun getRequests() = apiServices.getRequests()
    suspend fun acceptFollowRequest(body: AcceptOrRejectModel) = apiServices.acceptFollow(body)
    suspend fun rejectFollowRequest(body: AcceptOrRejectModel) = apiServices.rejectFollow(body)

    suspend fun getNotifications() = apiServices.getNotifications()
}