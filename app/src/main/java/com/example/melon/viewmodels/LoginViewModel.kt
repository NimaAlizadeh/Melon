package com.example.melon.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melon.models.GetUserResponse
import com.example.melon.models.LoginModel
import com.example.melon.models.LoginResponse
import com.example.melon.repositories.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository): ViewModel()
{
    val loading = MutableLiveData<Boolean>()
    val loginResponse = MutableLiveData<LoginResponse>()
    val userDataResponse = MutableLiveData<GetUserResponse>()

    fun loginUser(body: LoginModel) = viewModelScope.launch {
        loading.postValue(true)

        try{
            val response = loginRepository.loginUser(body)
            if(response.isSuccessful)
                loginResponse.postValue(response.body())
        }catch (e: Exception) {
            Log.d("------------------------------------------------------------", "exception : " + e.message)
        }

        loading.postValue(false)
    }

    fun getUserData(token: String) = viewModelScope.launch {
        loading.postValue(true)

        try {
            val response = loginRepository.getUserData(token)
            if(response.isSuccessful)
                userDataResponse.postValue(response.body())
            else{
                Log.d("LoginViewModel getUserData code:     ", response.code().toString())
                Log.d("LoginViewModel getUserData message:     ", response.message().toString())
            }
        }catch (e: Exception){
            Log.d("LoginViewModel getUserData try exception :     ", e.message.toString())
        }


        loading.postValue(false)
    }
}