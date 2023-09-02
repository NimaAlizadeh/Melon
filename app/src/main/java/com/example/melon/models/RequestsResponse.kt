package com.example.melon.models

data class RequestsResponse(
    val followerRequests: List<FollowModel> = emptyList(),
    val followingRequests: List<FollowModel> = emptyList(),
)