package com.example.melon.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class HomePostsResponse(
    val currentPage: Int,
    val posts: List<Post>,
    val totalCount: Int,
    val totalPages: Int
) {
    @Parcelize
    data class Post(
        val _id: String,
        val comments: List<CommentModel>,
        val created_time: String,
        val description: String,
        val images: List<String>,
        val last_updated: String,
        val likes: List<String>,
        val likesCount: Int,
        val time: String,
        val user: String,
        val username: String
    ): Parcelable
}