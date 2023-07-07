package com.example.melon.viewmodels

import androidx.lifecycle.ViewModel
import com.example.melon.repositories.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository): ViewModel()
{
    suspend fun getAllPostHome() = homeRepository.getAllPostHome()
}