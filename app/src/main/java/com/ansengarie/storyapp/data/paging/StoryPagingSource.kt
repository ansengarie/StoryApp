package com.ansengarie.storyapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ansengarie.storyapp.data.remote.ApiService
import com.ansengarie.storyapp.data.response.StoryResponse
import com.ansengarie.storyapp.utils.UserPreferences
import kotlinx.coroutines.flow.first

class StoryPagingSource(private val apiService: ApiService, private val preff: UserPreferences) :
    PagingSource<Int, StoryResponse>() {
    override fun getRefreshKey(state: PagingState<Int, StoryResponse>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResponse> {
        return try {
            val position = params.key ?: PAGE
            if (preff.getInfo().first().token.isNotEmpty()) {
                val response = apiService.getAllStories(
                    preff.getInfo().first().token,
                    position,
                    params.loadSize
                )
                LoadResult.Page(
                    data = response.body()?.listStory ?: emptyList(),
                    prevKey = if (position == PAGE) null else position - 1,
                    nextKey = if (response.body()?.listStory.isNullOrEmpty()) null else position + 1
                )
            } else {
                LoadResult.Error(Exception("Failed"))
            }


        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    private companion object {
        const val PAGE = 1
    }

}