package com.example.melon.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.melon.models.AddPostResponse
import com.example.melon.paging.AddPostsPagingSource
import com.example.melon.paging.SearchPostsPagingSource
import com.example.melon.repositories.AddPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(private val addPostRepository: AddPostRepository, @ApplicationContext context: Context): ViewModel()
{
    val loading = MutableLiveData<Boolean>()
    val addPostResponse = MutableLiveData<Response<AddPostResponse>>()

    fun doAddPost(time: RequestBody, description: RequestBody, images: List<MultipartBody.Part>) = viewModelScope.launch {
        loading.postValue(true)

        try{
            val response = addPostRepository.addPost(time, description, images)
            addPostResponse.postValue(response)

        }catch (e: Exception) {
            Log.d("------------------------------------------------------------", e.message.toString())
            Log.d("------------------------------------------------------------", e.message.toString())
        }

        loading.postValue(false)
    }




    private val pagingConfig = PagingConfig(
        pageSize = 10, // Adjust this based on your preference
        prefetchDistance = 2, // Adjust as needed
        enablePlaceholders = false
    )

//    private val pager = Pager(
//        config = pagingConfig,
//        pagingSourceFactory = { AddPostsPagingSource(context) }
//    )
//
//    val pagingFlow = pager.flow.cachedIn(viewModelScope)

    val addPostsList = Pager(pagingConfig){
        AddPostsPagingSource(context)
    }.flow.cachedIn(viewModelScope)
}