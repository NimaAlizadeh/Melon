package com.example.melon.models

data class HomePostsResponse(
    val currentPage: Int,
    val posts: List<Post>,
    val totalCount: Int,
    val totalPages: Int
) {
    data class Post(
        val _id: String,
        val comments: List<Comment>,
        val created_time: String,
        val description: String,
        val images: List<String>,
        val last_updated: String,
        val likes: List<String>,
        val likesCount: Int,
        val time: String,
        val user: String,
        val username: String
    ) {
        data class Comment(
            val comment: String,
            val time: String,
            val user_id: String,
            val username: String
        )
    }
}