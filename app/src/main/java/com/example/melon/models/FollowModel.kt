package com.example.melon.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FollowModel(
    val followers: Int = 0,
    val followings: Int = 0,
    val id: String = "",
    val name: String = "",
    val username: String = ""
): Parcelable