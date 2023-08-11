package com.example.melon.repositories

import com.example.melon.api.ApiServices
import javax.inject.Inject

class SearchRepository @Inject constructor(private val apiServices: ApiServices)
{
    suspend fun searchUser(searchTerm: String) = apiServices.searchUser(searchTerm)
}