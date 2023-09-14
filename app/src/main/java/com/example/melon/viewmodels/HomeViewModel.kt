package com.example.melon.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.melon.paging.HomePostsPagingSource
import com.example.melon.repositories.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository): ViewModel()
{

    val postsList = Pager(PagingConfig(1)){
        HomePostsPagingSource(homeRepository)
    }.flow.cachedIn(viewModelScope)


}