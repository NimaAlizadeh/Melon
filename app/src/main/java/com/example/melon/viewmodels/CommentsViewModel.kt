package com.example.melon.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melon.models.CommentResponse
import com.example.melon.models.JustStringResponse
import com.example.melon.models.LikeCommentModel
import com.example.melon.repositories.CommentsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(private val commentsRepository: CommentsRepository): ViewModel()
{
    val loading = MutableLiveData<Boolean>()
    val addCommentResponse = MutableLiveData<CommentResponse>()

    fun addComment(body: LikeCommentModel) = viewModelScope.launch {
        loading.postValue(true)

        try {
            val response = commentsRepository.addComment(body)
            if(response.isSuccessful)
                addCommentResponse.postValue(response.body())
            else{
                Log.d("CommentsViewModel code:     ", response.code().toString())
                Log.d("CommentsViewModel message:     ", response.message().toString())
            }
        }catch (e: Exception){
            Log.d("CommentsViewModel try exception :     ", e.message.toString())
        }

        loading.postValue(false)
    }
}