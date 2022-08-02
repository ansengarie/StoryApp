package com.ansengarie.storyapp.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ansengarie.storyapp.data.model.UserModel
import kotlinx.coroutines.launch

class PreferencesViewModel(private val pref: UserPreferences) : ViewModel() {

    fun getInfo(): LiveData<UserModel> {
        return pref.getInfo().asLiveData()
    }

    fun saveInfo(storyModel: UserModel) {
        viewModelScope.launch {
            pref.saveInfo(storyModel)
        }
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

}