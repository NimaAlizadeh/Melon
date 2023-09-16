package com.example.melon.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melon.models.*
import com.example.melon.repositories.FollowsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowsViewModel @Inject constructor(private val followsRepository: FollowsRepository) : ViewModel() {


    var followResponse = MutableLiveData<FollowResponse>()
    var removeResponse = MutableLiveData<FollowResponse>()
    var unFollowResponse = MutableLiveData<FollowResponse>()
    var cancelRequestResponse = MutableLiveData<FollowResponse>()
    var followLoading = MutableLiveData<Boolean>()

    fun addFollower(body: AddFollowerModel) = viewModelScope.launch {

        followLoading.postValue(true)

        try{
            val response = followsRepository.addFollower(body)
            if(response.isSuccessful) {
                followResponse.postValue(response.body())
            }
        }catch (e: Exception) {
            Log.d("addFollower - FollowsViewModel ------------------------------------------------------------", "exception : " + e.message)
        }

        followLoading.postValue(false)
    }

    fun removeFollower(body: RemoveFollowModel) = viewModelScope.launch {

        followLoading.postValue(true)

        try{
            val response = followsRepository.removeFollower(body)
            if(response.isSuccessful) {
                removeResponse.postValue(response.body())
            }
        }catch (e: Exception) {
            Log.d("removeFollower - FollowsViewModel ------------------------------------------------------------", "exception : " + e.message)
        }

        followLoading.postValue(false)
    }

    fun unFollow(body: UnFollowModel) = viewModelScope.launch {

        followLoading.postValue(true)

        try{
            val response = followsRepository.unFollow(body)
            if(response.isSuccessful) {
                unFollowResponse.postValue(response.body())
            }
        }catch (e: Exception) {
            Log.d("unFollow - FollowsViewModel ------------------------------------------------------------", "exception : " + e.message)
        }

        followLoading.postValue(false)
    }

    fun cancelRequest(body: RemoveFollowerModel) = viewModelScope.launch {

        followLoading.postValue(true)

        try{
            val response = followsRepository.cancelRequest(body)
            if(response.isSuccessful) {
                cancelRequestResponse.postValue(response.body())
            }
        }catch (e: Exception) {
            Log.d("cancelRequest - FollowsViewModel ------------------------------------------------------------", "exception : " + e.message)
        }

        followLoading.postValue(false)
    }
}