package com.ansengarie.storyapp.data.remote

import com.ansengarie.storyapp.data.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization")
        authHeader: String,
        @Query("page")
        page: Int,
        @Query("size")
        size: Int
    ): Response<UserResponse>

    @GET("stories?location=1")
    fun getAllStoriesWithLocation(
        @Header("Authorization")
        authHeader: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserResponse>

    @Multipart
    @POST("stories")
    fun addUser(
        @Header("Authorization")
        authHeader: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<UserResponse>
}