package com.example.melon.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.melon.models.HomePostsResponse
import com.example.melon.repositories.HomeRepository
import com.example.melon.repositories.SearchRepository
import javax.inject.Inject

class SearchPostsPagingSource @Inject constructor(private val repository: SearchRepository) : PagingSource<Int, HomePostsResponse.Post>()
{
    override fun getRefreshKey(state: PagingState<Int, HomePostsResponse.Post>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomePostsResponse.Post> {
        return try {
            val page = params.key ?: 1
            val response = repository.getAllSearchPosts(page, 3)
            if (response.isSuccessful) {
                val data = response.body()?.posts ?: emptyList()
                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (data.isEmpty()) null else page + 1
                )
            }else{
                return LoadResult.Error(Exception(response.message()))
            }
        } catch (e:Exception){
            LoadResult.Error(e)
        }
    }

}