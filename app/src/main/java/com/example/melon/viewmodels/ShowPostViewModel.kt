package com.example.melon.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melon.models.JustStringResponse
import com.example.melon.models.LikeCommentModel
import com.example.melon.models.PostResponse
import com.example.melon.models.RecyclerViewState
import com.example.melon.repositories.ShowPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowPostViewModel @Inject constructor(private val showPostRepository: ShowPostRepository) : ViewModel()
{
    val loading = MutableLiveData<Boolean>()
    val allPostsResponseList = MutableLiveData<PostResponse>()
    val likePostResponse = MutableLiveData<JustStringResponse>()
    val leftPosition = MutableLiveData<(RecyclerViewState)>()

    fun loadPostsWithId(userId: String) = viewModelScope.launch {
        loading.postValue(true)
        try{

            val response = showPostRepository.getPostsWithId(userId)
            if(response.isSuccessful) {
                if (response.body()!!.success) {
                    allPostsResponseList.postValue(response.body())
                }
            }

        }catch (e: Exception) {
            Log.d("loadPosts - ShowPostViewModel ------------------------------------------------------------", "exception : " + e.message)
        }
        loading.postValue(false)
    }

    fun loadPostsWithToken() = viewModelScope.launch {
        loading.postValue(true)
        try{

            val response = showPostRepository.getPostsWithToken()
            if(response.isSuccessful) {
                if (response.body()!!.success) {
                    allPostsResponseList.postValue(response.body())
                }
            }

        }catch (e: Exception) {
            Log.d("loadPosts - ShowPostViewModel ------------------------------------------------------------", "exception : " + e.message)
        }

        loading.postValue(false)
    }

    fun likePost(body: LikeCommentModel) = viewModelScope.launch {

        try{

            val response = showPostRepository.likePost(body)
            if(response.isSuccessful) {
                likePostResponse.postValue(response.body())
            }

        }catch (e: Exception) {
            Log.d("likePost - ShowPostViewModel ------------------------------------------------------------", "exception : " + e.message)
        }

    }
}