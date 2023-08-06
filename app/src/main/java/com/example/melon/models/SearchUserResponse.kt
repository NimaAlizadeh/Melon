package com.example.melon.models

data class SearchUserResponse(
    val success: Boolean,
    val users: List<User>
)