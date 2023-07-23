package com.example.melon.repositories

import com.example.melon.api.ApiServices
import javax.inject.Inject

class SearchRepository @Inject constructor(private val apiServices: ApiServices)
{
    suspend fun searchUser(token: String, searchTerm: String) = apiServices.searchUser(token, searchTerm)
}