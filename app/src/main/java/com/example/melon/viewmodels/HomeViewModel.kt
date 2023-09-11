package com.example.melon.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.melon.models.GetUserResponse
import com.example.melon.paging.HomePostsPagingSource
import com.example.melon.repositories.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository): ViewModel()
{
//    val PostsDataResponse = MutableLiveData<GetUserResponse>()
//    val loading = MutableLiveData<Boolean>()
//
//    fun loadData() = viewModelScope.launch {
//        loading.postValue(true)
//
//        try {
//            val response = homeRepository.getUserData()
//            if(response.isSuccessful)
//                PostsDataResponse.postValue(response.body())
//            else{
//                Log.d("Home view model code:     ", response.code().toString())
//                Log.d("Home view model message:     ", response.message().toString())
//            }
//        }catch (e: Exception){
//            Log.d("Home view model try exception :     ", e.message.toString())
//        }
//
//
//        loading.postValue(false)
//    }


    val postsList = Pager(PagingConfig(1)){
        HomePostsPagingSource(homeRepository)
    }.flow.cachedIn(viewModelScope)


}