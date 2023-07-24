package com.example.melon.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserX(
    val bio: String = "",
    val id: String = "",
    val posts: List<Post> = emptyList(),
    val private: Boolean = false,
    val username: String = "",
    val followers: Int = 0,
    val followings: Int = 0,
) : Parcelable