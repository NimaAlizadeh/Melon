package com.example.melon.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melon.models.SignUpResponse
import com.example.melon.models.UserModel
import com.example.melon.repositories.SignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(private val signUpRepository: SignUpRepository): ViewModel()
{
    val loading = MutableLiveData<Boolean>()
    val signUpResponse = MutableLiveData<SignUpResponse>()

    fun signUpUser(body: UserModel) = viewModelScope.launch {
        loading.postValue(true)

        try{
            val response = signUpRepository.signUpUser(body)
            if(response.isSuccessful)
                signUpResponse.postValue(response.body())
        }catch (e: Exception) {
            Log.d("mashti------------------------------------------------------------", "error from exception")
        }

        loading.postValue(false)
    }
}