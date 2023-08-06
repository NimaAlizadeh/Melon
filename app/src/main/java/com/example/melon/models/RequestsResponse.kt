package com.example.melon.models

data class RequestsResponse(
    val followerRequests: List<Follow>,
    val followingRequests: List<Follow>,
)