package com.example.melon.models

data class User(
    var __v: Int = 0,
    var _id: String = "",
    var bio: String = "",
    var birthday: String = "",
    var created_time: String = "",
    var email: String = "",
    var followerRequests: List<FollowModel> = emptyList(),
    var followers: List<FollowModel> = emptyList(),
    var followings: List<FollowModel> = emptyList(),
    var gender: String = "",
    var private: Boolean = false,
    var username: String = "",
    var posts: List<Post> = emptyList()
)