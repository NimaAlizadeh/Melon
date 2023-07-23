package com.example.melon.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melon.models.Post
import com.example.melon.models.SearchUserResponse
import com.example.melon.repositories.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: SearchRepository): ViewModel()
{
    val loading = MutableLiveData<Boolean> ()
    val userSearchLiveList = MutableLiveData<SearchUserResponse> ()
//    val explorePostsLiveList = MutableLiveData<Post>()

    @SuppressLint("SuspiciousIndentation")
    fun loadUserSearch(token: String, searchTerm: String) = viewModelScope.launch {
        loading.postValue(true)

        try {
            val response = searchRepository.searchUser(token, searchTerm)
            if(response.isSuccessful)
                userSearchLiveList.postValue(response.body())
            else{
                Log.d("Search view model code:     ", response.code().toString())
                Log.d("Search view model message:     ", response.message().toString())
            }
        }catch (e: Exception){
            Log.d("Search view model try exception :     ", e.message.toString())
        }

        loading.postValue(false)
    }


}