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
    var unFollowResponse = MutableLiveData<FollowResponse>()
    var cancelRequestResponse = MutableLiveData<FollowResponse>()

    fun loadUserDataWithToken() = viewModelScope.launch {
        loading.postValue(true)

        try{
            val response = profileRepository.getUserData()
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

    fun loadPostsWithId(userId: String) = viewModelScope.launch {

        try{

            val response = profileRepository.getPostsWithId(userId)
            if(response.isSuccessful) {
                if (response.body()!!.success) {
                    allPostsResponseList.postValue(response.body())
                }
            }

        }catch (e: Exception) {
            Log.d("loadPosts - profileViewModel ------------------------------------------------------------", "exception : " + e.message)
        }

    }

    fun loadPostsWithToken() = viewModelScope.launch {

        try{

            val response = profileRepository.getPostsWithToken()
            if(response.isSuccessful) {
                if (response.body()!!.success) {
                    allPostsResponseList.postValue(response.body())
                }
            }

        }catch (e: Exception) {
            Log.d("loadPosts - profileViewModel ------------------------------------------------------------", "exception : " + e.message)
        }

    }

    fun loadUserDataWithId(token: String) = viewModelScope.launch{
        loading.postValue(true)

        try{
            val response = profileRepository.getUserDataWithId(token)
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

    fun addFollower(body: AddFollowerModel) = viewModelScope.launch {

        followLoading.postValue(true)

        try{
            val response = profileRepository.addFollower(body)
            if(response.isSuccessful) {
                followResponse.postValue(response.body())
            }
        }catch (e: Exception) {
            Log.d("addFollower - profileViewModel ------------------------------------------------------------", "exception : " + e.message)
        }

        followLoading.postValue(false)
    }

    fun unFollow(body: UnFollowModel) = viewModelScope.launch {

        followLoading.postValue(true)

        try{
            val response = profileRepository.unFollow(body)
            if(response.isSuccessful) {
                unFollowResponse.postValue(response.body())
            }
        }catch (e: Exception) {
            Log.d("unFollow - profileViewModel ------------------------------------------------------------", "exception : " + e.message)
        }

        followLoading.postValue(false)
    }

    fun cancelRequest(body: RemoveFollowerModel) = viewModelScope.launch {

        followLoading.postValue(true)

        try{
            val response = profileRepository.cancelRequest(body)
            if(response.isSuccessful) {
                cancelRequestResponse.postValue(response.body())
            }
        }catch (e: Exception) {
            Log.d("cancelRequest - profileViewModel ------------------------------------------------------------", "exception : " + e.message)
        }

        followLoading.postValue(false)
    }
}