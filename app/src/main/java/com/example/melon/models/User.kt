package com.example.melon.models

data class User(
    var id: String = "",
    var bio: String = "",
    var birthday: String = "",
    var created_time: String = "",
    var email: String = "",
    var followers: List<FollowModel> = emptyList(),
    var followings: List<FollowModel> = emptyList(),
    var followersCount: Int = 0,
    var followingsCount: Int = 0,
    var gender: String = "",
    var private: Boolean = false,
    var username: String = "",
    var name: String = "",
)