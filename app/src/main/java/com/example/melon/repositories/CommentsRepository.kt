package com.example.melon.repositories

import com.example.melon.api.ApiServices
import com.example.melon.models.LikeCommentModel
import javax.inject.Inject

class CommentsRepository @Inject constructor(private val apiServices: ApiServices)
{
    suspend fun addComment(body: LikeCommentModel) = apiServices.addComment(body)
}