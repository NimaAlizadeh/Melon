package com.example.melon.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melon.models.GetUserResponse
import com.example.melon.models.Post
import com.example.melon.models.UserResponseWithId
import com.example.melon.repositories.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) : ViewModel() {
    var allPostsResponseList = MutableLiveData<List<Post>>()
    var userDataResponse = MutableLiveData<GetUserResponse>()
    var userDataResponsewithId = MutableLiveData<UserResponseWithId>()
    var loading = MutableLiveData<Boolean>()

    fun loadPosts(token: String) = viewModelScope.launch {
        loading.postValue(true)

        try{
            val response1 = profileRepository.getUserData(token)
            if(response1.isSuccessful) {
                if (response1.body()!!.success) {
                    userDataResponse.postValue(response1.body())
                }
            }

            val response = profileRepository.getAllPost(token)
            if(response.isSuccessful) {
                if (response.body()!!.success) {
                    allPostsResponseList.postValue(response.body()!!.data.posts)
                }
            }
        }catch (e: Exception) {
            Log.d("loadPosts - profileViewModel ------------------------------------------------------------", "exception : " + e.message)
        }

        loading.postValue(false)
    }

    fun loadUserDataWithId(userId: String, token: String) = viewModelScope.launch{
        loading.postValue(true)

        try{
            val response = profileRepository.getUserDataWithId(userId, token)
            if(response.isSuccessful) {
                if (response.body()!!.success) {
                    userDataResponsewithId.postValue(response.body())
                }
            }
        }catch (e: Exception) {
            Log.d("loadUserDataWithId - profileViewModel ------------------------------------------------------------", "exception : " + e.message)
        }

        loading.postValue(false)
    }
}