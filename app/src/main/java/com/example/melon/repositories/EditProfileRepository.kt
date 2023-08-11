package com.example.melon.repositories

import com.example.melon.api.ApiServices
import com.example.melon.models.EditProfileModel
import javax.inject.Inject

class EditProfileRepository @Inject constructor(private val apiServices: ApiServices)
{
    suspend fun getUserData() = apiServices.getUserData()
    suspend fun editUserProfile(body: EditProfileModel) = apiServices.editProfile(body)
}