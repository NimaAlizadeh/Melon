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
//
//    @GET("post")
//    suspend fun getAllPost(@Header("auth-token") token: String): Response<PostResponse>
    @GET("post/{userId}")
    suspend fun getPostsWithId(@Header("auth-token") token: String, @Path("userId") userId: String): Response<PostResponse>

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
    suspend fun addFollower(@Header("auth-token") token: String, @Body body: AddFollowerModel): Response<FollowResponse>

    @POST("auth/unfollow")
    suspend fun unFollow(@Header("auth-token") token: String, @Body body: UnFollowModel): Response<FollowResponse>

    @POST("auth/removeFollower")
    suspend fun removeFollower(@Header("auth-token") token: String, @Body body: RemoveFollowerModel): Response<FollowResponse>

    @POST("auth/cancelRequest")
    suspend fun cancelRequest(@Header("auth-token") token: String, @Body body: RemoveFollowerModel): Response<FollowResponse>

    @GET("auth/getRequests")
    suspend fun getRequests(@Header("auth-token") token: String): Response<RequestsResponse>

    @POST("auth/acceptRequest")
    suspend fun acceptFollow(@Header("auth-token") token: String, @Body body: AcceptOrRejectModel): Response<AddPostResponse>
    // response is like the same as addPostResponse and it just gets success and message
    @POST("auth/rejectRequest")
    suspend fun rejectFollow(@Header("auth-token") token: String, @Body body: AcceptOrRejectModel) : Response<AddPostResponse>
    // response is like the same as addPostResponse and it just gets success and message


}
