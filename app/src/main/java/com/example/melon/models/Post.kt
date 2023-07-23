package com.example.melon.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val _id: String,
    val comments: List<String>,
    val created_time: String,
    val description: String,
    val images: List<String>,
    val last_updated: String
): Parcelable