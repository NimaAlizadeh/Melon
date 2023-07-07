package com.example.melon.models

data class Post(
    val _id: String,
    val comments: List<Any>,
    val created_time: String,
    val description: String,
    val images: List<String>,
    val last_updated: String
)