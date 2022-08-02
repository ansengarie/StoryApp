package com.ansengarie.storyapp.data.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ansengarie.storyapp.data.response.StoryResponse

class StoryPagingViewModel(repo: StoryPagingRepository) : ViewModel() {
    val getAllStories: LiveData<PagingData<StoryResponse>> =
        repo.getAllStories().cachedIn(viewModelScope)
}