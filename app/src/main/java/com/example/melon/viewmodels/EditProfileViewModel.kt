package com.example.melon.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melon.models.AddPostResponse
import com.example.melon.models.EditProfileModel
import com.example.melon.models.GetUserResponse
import com.example.melon.repositories.EditProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val editProfileRepository: EditProfileRepository): ViewModel()
{
    val editLoading = MutableLiveData<Boolean>()
    val userDataLoading = MutableLiveData<Boolean>()
    val editProfileResponse = MutableLiveData<Response<AddPostResponse>>()
    val userDataResponse = MutableLiveData<Response<GetUserResponse>>()

    fun loadUserData(token: String) = viewModelScope.launch {
        userDataLoading.postValue(true)

        try{
            val response = editProfileRepository.getUserData(token)
            userDataResponse.postValue(response)

        }catch (e: Exception) {
            Log.d("------------------------------------------------------------", e.message.toString())
        }

        userDataLoading.postValue(false)
    }

    fun doEditUserProfile(token: String, body: EditProfileModel) = viewModelScope.launch {
        editLoading.postValue(true)

        try{
            val response = editProfileRepository.editUserProfile(token, body)
            editProfileResponse.postValue(response)

        }catch (e: Exception) {
            Log.d("------------------------------------------------------------", e.message.toString())
        }

        editLoading.postValue(false)
    }
}