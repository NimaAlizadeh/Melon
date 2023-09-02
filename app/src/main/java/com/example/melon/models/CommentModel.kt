package com.example.melon.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CommentModel(
    var user_id: String = "",
    var username: String = "",
    var comment: String = "",
    var time: String
): Parcelable