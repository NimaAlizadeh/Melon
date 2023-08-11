package com.example.melon.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melon.models.AddPostResponse
import com.example.melon.models.JustStringResponse
import com.example.melon.repositories.ChangeAvatarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ChangeAvatarViewModel @Inject constructor(private val changeAvatarRepository: ChangeAvatarRepository): ViewModel()
{
    val loading = MutableLiveData<Boolean>()
    val changeAvatarResponse = MutableLiveData<Response<JustStringResponse>>()

    fun doChangeAvatar(avatar: MultipartBody.Part) = viewModelScope.launch {
        loading.postValue(true)

        try{
            val response = changeAvatarRepository.changeAvatar(avatar)
            changeAvatarResponse.postValue(response)

        }catch (e: Exception) {
            Log.d("------------------------------------------------------------", e.message.toString())
        }

        loading.postValue(false)
    }
}