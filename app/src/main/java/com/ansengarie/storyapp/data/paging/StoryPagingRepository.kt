package com.ansengarie.storyapp.data.paging

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.ansengarie.storyapp.data.remote.ApiService
import com.ansengarie.storyapp.data.response.StoryResponse
import com.ansengarie.storyapp.utils.UserPreferences

class StoryPagingRepository(
    private val apiService: ApiService,
    private val preff: UserPreferences
) {
    fun getAllStories(): LiveData<PagingData<StoryResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, preff)
            }
        ).liveData
    }
}