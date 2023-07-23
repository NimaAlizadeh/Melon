package com.example.melon.models

data class EditProfileModel(
    var bio: String = "",
    var birthday: String = "",
    var email: String = "",
    var gender: String = "",
    var newPassword: String = "",
    var oldPassword: String = "",
    var username: String = "",
    var private: Boolean = false
)