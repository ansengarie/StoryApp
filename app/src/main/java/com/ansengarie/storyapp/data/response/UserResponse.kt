package com.ansengarie.storyapp.data.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

    @field:SerializedName("loginResult")
    val loginResult: LoginResponse,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("listStory")
    val listStory: List<StoryResponse>

)
