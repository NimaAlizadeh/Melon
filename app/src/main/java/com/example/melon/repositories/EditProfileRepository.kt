package com.example.melon.repositories

import com.example.melon.api.ApiServices
import com.example.melon.models.EditProfileModel
import javax.inject.Inject

class EditProfileRepository @Inject constructor(private val apiServices: ApiServices)
{
    suspend fun getUserData(token: String) = apiServices.getUserData(token)
    suspend fun editUserProfile(token: String, body: EditProfileModel) = apiServices.editProfile(token, body)
}