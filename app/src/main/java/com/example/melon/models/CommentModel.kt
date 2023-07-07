package com.example.melon.models

import android.graphics.drawable.Drawable

data class CommentModel(
    var username: String = "",
    var builtTime: String = "",
    var commentText: String = "",
    var userImage: Drawable
)
