package com.example.melon.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melon.models.AddPostResponse
import com.example.melon.repositories.AddPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(private val addPostRepository: AddPostRepository): ViewModel()
{
    val loading = MutableLiveData<Boolean>()
    val addPostResponse = MutableLiveData<Response<AddPostResponse>>()

    fun doAddPost(token: String, description: RequestBody, images: List<MultipartBody.Part>) = viewModelScope.launch {
        loading.postValue(true)

        try{
            val response = addPostRepository.addPost(token, description, images)
            addPostResponse.postValue(response)

        }catch (e: Exception) {
            Log.d("------------------------------------------------------------", e.message.toString())
            Log.d("------------------------------------------------------------", e.message.toString())
        }

        loading.postValue(false)
    }
}