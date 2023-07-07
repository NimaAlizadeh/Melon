package com.example.melon.repositories

import com.example.melon.api.ApiServices
import com.example.melon.models.UserModel
import javax.inject.Inject

class SignUpRepository @Inject constructor(private val apiServices: ApiServices)
{
    suspend fun signUpUser(body: UserModel) = apiServices.signUpUser(body)
}