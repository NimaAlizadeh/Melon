package com.example.melon.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melon.models.AcceptOrRejectModel
import com.example.melon.models.AddPostResponse
import com.example.melon.models.FollowModel
import com.example.melon.models.RequestsResponse
import com.example.melon.repositories.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.http.Body
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val notificationRepository: NotificationRepository) : ViewModel()
{
    val loading = MutableLiveData<Boolean>()
    val requestsResponse = MutableLiveData<RequestsResponse>()
    val acceptResponse = MutableLiveData<AddPostResponse>()
    val rejectResponse = MutableLiveData<AddPostResponse>()
    val requestLoading = MutableLiveData<Boolean>()

    fun loadRequests() = viewModelScope.launch {
        loading.postValue(true)

        try {
            val response = notificationRepository.getRequests()
            if(response.isSuccessful)
                requestsResponse.postValue(response.body())
            else{
                Log.d("Notification view model code:     ", response.code().toString())
                Log.d("Notification view model message:     ", response.message().toString())
            }
        }catch (e: Exception){
            Log.d("Notification view model try exception :     ", e.message.toString())
        }

        loading.postValue(false)
    }

    fun acceptFollowRequest(body: AcceptOrRejectModel) = viewModelScope.launch {
        requestLoading.postValue(true)

        try {
            val response = notificationRepository.acceptFollowRequest(body)
            if(response.isSuccessful)
                acceptResponse.postValue(response.body())
            else{
                Log.d("Notification view model acceptFollowRequest code:     ", response.code().toString())
                Log.d("Notification view model acceptFollowRequest message:     ", response.message().toString())
            }
        }catch (e: Exception){
            Log.d("Notification view model acceptFollowRequest try exception :     ", e.message.toString())
        }

        requestLoading.postValue(false)
    }

    fun rejectFollowRequest(body: AcceptOrRejectModel) = viewModelScope.launch {
        requestLoading.postValue(true)

        try {
            val response = notificationRepository.rejectFollowRequest(body)
            if(response.isSuccessful)
                acceptResponse.postValue(response.body())
            else{
                Log.d("Notification view model rejectFollowRequest code:     ", response.code().toString())
                Log.d("Notification view model rejectFollowRequest message:     ", response.message().toString())
            }
        }catch (e: Exception){
            Log.d("Notification view model rejectFollowRequest try exception :     ", e.message.toString())
        }

        requestLoading.postValue(false)
    }
}