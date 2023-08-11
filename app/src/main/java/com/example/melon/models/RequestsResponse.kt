package com.example.melon.models

data class RequestsResponse(
    val followerRequests: List<Follow> = emptyList(),
    val followingRequests: List<Follow> = emptyList(),
)