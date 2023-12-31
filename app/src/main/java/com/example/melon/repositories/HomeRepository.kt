package com.example.melon.repositories

import com.example.melon.api.ApiServices
import com.example.melon.models.LikeCommentModel
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiServices: ApiServices)
{
    suspend fun getHomePosts(page: Int, limit: Int) = apiServices.getHomePosts(page, limit)

    suspend fun likePost(body: LikeCommentModel) = apiServices.likePost(body)
}