package com.example.melon.paging

import android.content.ContentResolver
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.melon.models.HomePostsResponse
import com.example.melon.repositories.HomeRepository
import javax.inject.Inject

class AddPostsPagingSource @Inject constructor(private val context: Context) : PagingSource<Int, String>()
{
    override fun getRefreshKey(state: PagingState<Int, String>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        val page = params.key ?: 0
        val pageSize = params.loadSize

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val orderBy = MediaStore.Video.Media.DATE_TAKEN
        val cursor = context.contentResolver.query(uri, projection, null, null, "$orderBy DESC")
//        val columnIndexData = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

//        while (cursor.moveToNext())
//            imagesList.add(cursor.getString(columnIndexData))


        val offset = page * pageSize
        val limit = pageSize


        val imagesList = mutableListOf<String>()

        cursor?.use { c ->
            // Move the cursor to the start of the requested page
            val startPos = page * pageSize
            if (c.moveToPosition(startPos)) {
                do {
                    val columnIndexData = c.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                    imagesList.add(c.getString(columnIndexData))
                } while (c.moveToNext() && imagesList.size < limit)
            }
        }

        cursor?.close()

        val prevKey = if (page == 0) null else page - 1
        val nextKey = if (imagesList.size < pageSize) null else page + 1

        return LoadResult.Page(
            data = imagesList,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }

}