package com.example.melon.models

data class PostResponse(
    val posts: List<Post>,
    val success: Boolean
)