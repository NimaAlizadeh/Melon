package com.example.melon.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.melon.models.JustStringResponse
import com.example.melon.models.LikeCommentModel
import com.example.melon.models.RecyclerViewState
import com.example.melon.paging.HomePostsPagingSource
import com.example.melon.repositories.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository): ViewModel()
{

    val leftPosition = MutableLiveData<(RecyclerViewState)>()
    val likePostResponse = MutableLiveData<JustStringResponse>()

    val postsList = Pager(PagingConfig(1)){
        HomePostsPagingSource(homeRepository)
    }.flow.cachedIn(viewModelScope)


    fun likePost(body: LikeCommentModel) = viewModelScope.launch {

        try{

            val response = homeRepository.likePost(body)
            if(response.isSuccessful) {
                likePostResponse.postValue(response.body())
            }

        }catch (e: Exception) {
            Log.d("likePost - HomeViewModel ------------------------------------------------------------", "exception : " + e.message)
        }

    }

}