package com.example.melon.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}