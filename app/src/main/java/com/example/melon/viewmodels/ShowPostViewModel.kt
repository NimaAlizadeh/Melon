package com.example.melon.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melon.models.UserResponseWithId
import com.example.melon.repositories.ShowPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowPostViewModel @Inject constructor(private val showPostRepository: ShowPostRepository) : ViewModel()
{
    val loading = MutableLiveData<Boolean>()
    val userResponse = MutableLiveData<UserResponseWithId>()


    fun loadUserData(token: String, userId: String) = viewModelScope.launch {
        loading.postValue(true)

        try{
            val response = showPostRepository.getUserDataWithId(userId, token)
            if(response.isSuccessful) {
                if (response.body()!!.success) {
                    Log.d("sssssssssssssssssssssssssssssssssssssssss", "hello we got heree")
                    userResponse.postValue(response.body())
                    Log.d("sssssssssssssssssssssssssssssssssssssssss", "hello we got heree 2")
                }
            }
        }catch (e: Exception) {
            Log.d("loadUserDataWithId - ShowPostViewModel ------------------------------------------------------------", "exception : " + e.message)
        }

        loading.postValue(false)
    }
}