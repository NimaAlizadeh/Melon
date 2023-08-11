package com.example.melon.models

data class FollowData(
    val followers: List<Follow> = emptyList(),
    val followings: List<Follow> = emptyList(),
    val followerRequests: List<Follow>,
    val followingRequests: List<Follow>
)