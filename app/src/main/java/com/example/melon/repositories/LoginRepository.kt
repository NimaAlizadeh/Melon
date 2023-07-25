package com.example.melon.repositories

import com.example.melon.api.ApiServices
import com.example.melon.models.LoginModel
import javax.inject.Inject

class LoginRepository @Inject constructor(private val apiServices: ApiServices)
{
    suspend fun loginUser(body: LoginModel) = apiServices.loginUser(body)
    suspend fun getUserData(token: String) = apiServices.getUserData(token)
}