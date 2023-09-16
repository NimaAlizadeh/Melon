package com.example.melon.repositories

import com.example.melon.api.ApiServices
import com.example.melon.models.LikeCommentModel
import javax.inject.Inject

class ShowSinglePostRepository @Inject constructor(private val apiServices: ApiServices)
{
    suspend fun likePost(body: LikeCommentModel) = apiServices.likePost(body)
}