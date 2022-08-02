package com.ansengarie.storyapp.data.paging

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ansengarie.storyapp.utils.Injection
import com.ansengarie.storyapp.utils.UserPreferences

class StoryPagingViewModelFactory(
    private val context: Context,
    private val preff: UserPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryPagingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryPagingViewModel(Injection.providePagingRepository(context, preff)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}