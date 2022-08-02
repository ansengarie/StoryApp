package com.ansengarie.storyapp.utils

import android.content.Context
import com.ansengarie.storyapp.data.paging.StoryPagingRepository
import com.ansengarie.storyapp.data.remote.ApiConfig

object Injection {
    fun providePagingRepository(context: Context, preff: UserPreferences): StoryPagingRepository {
        return StoryPagingRepository(ApiConfig.getApiService(), preff)
    }
}