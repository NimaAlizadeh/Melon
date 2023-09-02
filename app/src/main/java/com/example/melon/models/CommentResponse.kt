package com.example.melon.models

data class CommentResponse(
    val `data`: Data,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val comments: List<CommentModel>
    )
}