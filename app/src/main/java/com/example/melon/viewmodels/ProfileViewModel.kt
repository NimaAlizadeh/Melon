package com.example.melon.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melon.models.*
import com.example.melon.repositories.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) : ViewModel() {
    var allPostsResponseList = MutableLiveData<PostResponse>()
    var userDataResponseWithToken = MutableLiveData<GetUserResponse>()
    var userDataResponseWithId = MutableLiveData<UserResponseWithId>()
    var loading = MutableLiveData<Boolean>()
    var followLoading = MutableLiveData<Boolean>()
    var followResponse = MutableLiveData<FollowResponse>()

    fun loadUserDataWithToken(token: String) = viewModelScope.launch {
        loading.postValue(true)

        try{
            val response = profileRepository.getUserData(token)
            if(response.isSuccessful) {
                if (response.body()!!.success) {
                    userDataResponseWithToken.postValue(response.body())
                }
            }

        }catch (e: Exception) {
            Log.d("loadPosts - profileViewModel ------------------------------------------------------------", "exception : " + e.message)
        }

        loading.postValue(false)
    }

    fun loadPostsWithId(token: String, userId: String) = viewModelScope.launch {

        try{

            val response = profileRepository.getPostsWithId(token, userId)
            if(response.isSuccessful) {
                if (response.body()!!.success) {
                    allPostsResponseList.postValue(response.body())
                }
            }

        }catch (e: Exception) {
            Log.d("loadPosts - profileViewModel ------------------------------------------------------------", "exception : " + e.message)
        }

    }

    fun loadUserDataWithId(userId: String, token: String) = viewModelScope.launch{
        loading.postValue(true)

        try{
            val response = profileRepository.getUserDataWithId(userId, token)
            if(response.isSuccessful) {
                if (response.body()!!.success) {
                    userDataResponseWithId.postValue(response.body())
                }
            }
        }catch (e: Exception) {
            Log.d("loadUserDataWithId - profileViewModel ------------------------------------------------------------", "exception : " + e.message)
        }

        loading.postValue(false)
    }

    fun addFollower(token: String, body: AddFollowerModel) = viewModelScope.launch {

        followLoading.postValue(true)

        try{
            val response = profileRepository.addFollower(token,body)
            if(response.isSuccessful) {
                followResponse.postValue(response.body())
            }
        }catch (e: Exception) {
            Log.d("addFollower - profileViewModel ------------------------------------------------------------", "exception : " + e.message)
        }

        followLoading.postValue(false)
    }
}