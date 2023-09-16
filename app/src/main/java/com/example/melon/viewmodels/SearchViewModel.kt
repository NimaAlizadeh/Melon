package com.example.melon.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.melon.models.SearchUserResponse
import com.example.melon.paging.SearchPostsPagingSource
import com.example.melon.repositories.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: SearchRepository): ViewModel()
{
    val loading = MutableLiveData<Boolean> ()
    val userSearchLiveList = MutableLiveData<SearchUserResponse> ()

    @SuppressLint("SuspiciousIndentation")
    fun loadUserSearch(searchTerm: String) = viewModelScope.launch {
        loading.postValue(true)

        try {
            val response = searchRepository.searchUser(searchTerm)
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



    val searchPostsList = Pager(PagingConfig(1)){
        SearchPostsPagingSource(searchRepository)
    }.flow.cachedIn(viewModelScope)

}