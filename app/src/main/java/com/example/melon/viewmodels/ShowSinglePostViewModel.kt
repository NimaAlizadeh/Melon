package com.example.melon.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melon.models.JustStringResponse
import com.example.melon.models.LikeCommentModel
import com.example.melon.repositories.ShowSinglePostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowSinglePostViewModel @Inject constructor(private val repository: ShowSinglePostRepository): ViewModel()
{

    val likePostResponse = MutableLiveData<JustStringResponse>()

    fun likePost(body: LikeCommentModel) = viewModelScope.launch {

        try{

            val response = repository.likePost(body)
            if(response.isSuccessful) {
                likePostResponse.postValue(response.body())
            }

        }catch (e: Exception) {
            Log.d("likePost - ShowSinglePostViewModel ------------------------------------------------------------", "exception : " + e.message)
        }

    }
}