package com.example.melon.models

data class NotificationResponse(
    val notifications: List<Notification>,
    val success: Boolean
) {
    data class Notification(
        val __v: Int,
        val _id: String,
        val action: String,
        val message: String,
        val performer: Performer,
        val time: String,
        val user: String
    ) {
        data class Performer(
            val _id: String,
            val username: String
        )
    }
}