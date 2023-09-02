package com.example.melon.repositories

import com.example.melon.api.ApiServices
import com.example.melon.models.LikeCommentModel
import javax.inject.Inject

class ShowPostRepository @Inject constructor(private val apiServices: ApiServices)
{
    suspend fun getPostsWithId(userId: String) = apiServices.getPostsWithId(userId)
    suspend fun getPostsWithToken() = apiServices.getPostsWithToken()
    suspend fun likePost(body: LikeCommentModel) = apiServices.likePost(body)
}