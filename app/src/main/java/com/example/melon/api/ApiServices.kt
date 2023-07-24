package com.example.melon.api

import com.example.melon.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiServices {

    @POST("auth/login")
    suspend fun loginUser(@Body body: LoginModel): Response<LoginResponse>

    @POST("auth/register")
    suspend fun signUpUser(@Body body: UserModel): Response<SignUpResponse>

    @GET("post")
    suspend fun getAllPost(@Header("auth-token") token: String): Response<PostResponse>

    @GET("")
    suspend fun getAllPostHome()

    @Multipart
    @POST("post/create")
    suspend fun addPost(@Header("auth-token") token: String, @Part("description") description: RequestBody, @Part images: List<MultipartBody.Part>): Response<AddPostResponse>
    // response is like the same as addPostResponse and it just gets success and message

    @PUT("auth/edit")
    suspend fun editProfile(@Header("auth-token") token: String, @Body body: EditProfileModel): Response<AddPostResponse>
    // response is like the same as addPostResponse and it just gets success and message

    @GET("auth/getUser")
    suspend fun getUserData(@Header("auth-token") token: String): Response<GetUserResponse>

    @GET("auth/getUser/{userId}")
    suspend fun getUserDataWithId(@Path("userId") userId: String, @Header("auth-token") token: String): Response<UserResponseWithId>

    @Multipart
    @POST("auth/avatars")
    suspend fun changeAvatar(@Header("auth-token") token: String, @Part avatar: MultipartBody.Part): Response<JustStringResponse>
    // response is like the same as addPostResponse and it just gets success and message

    @GET("auth/getbyusername")
    suspend fun searchUser(@Header("auth-token") token: String, @Query("searchTerm") searchTerm: String): Response<SearchUserResponse>

    @POST("auth/addFollower")
    suspend fun addFollower(@Header("auth-token") token: String, @Body body: AddFollowerModel): Response<AddPostResponse>

}
