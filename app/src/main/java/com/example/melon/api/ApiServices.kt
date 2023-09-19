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
    suspend fun getPostsWithToken(): Response<PostResponse>

    @GET("post/{userId}")
    suspend fun getPostsWithId(@Path("userId") userId: String): Response<PostResponse>

    @Multipart
    @POST("post/create")
    suspend fun addPost(@Part("time") time: RequestBody,@Part("description") description: RequestBody, @Part images: List<MultipartBody.Part>): Response<AddPostResponse>
    // response is like the same as addPostResponse and it just gets success and message

    @PUT("auth/edit")
    suspend fun editProfile(@Body body: EditProfileModel): Response<AddPostResponse>
    // response is like the same as addPostResponse and it just gets success and message

    @GET("auth/getUser")
    suspend fun getUserData(): Response<GetUserResponse>

    @GET("auth/getUser/{userId}")
    suspend fun getUserDataWithId(@Path("userId") userId: String): Response<UserResponseWithId>

    @Multipart
    @POST("auth/avatars")
    suspend fun changeAvatar(@Part avatar: MultipartBody.Part): Response<JustStringResponse>
    // response is like the same as addPostResponse and it just gets success and message

    @GET("auth/getbyusername")
    suspend fun searchUser(@Query("searchTerm") searchTerm: String): Response<SearchUserResponse>

    @POST("auth/addFollower")
    suspend fun addFollower(@Body body: AddFollowerModel): Response<FollowResponse>

    @POST("auth/unfollow")
    suspend fun unFollow(@Body body: UnFollowModel): Response<FollowResponse>

    @POST("auth/removeFollower")
    suspend fun removeFollower(@Body body: RemoveFollowModel): Response<FollowResponse>

    @POST("auth/cancelRequest")
    suspend fun cancelRequest(@Body body: RemoveFollowerModel): Response<FollowResponse>

    @GET("auth/getRequests")
    suspend fun getRequests(): Response<RequestsResponse>

    @POST("auth/acceptRequest")
    suspend fun acceptFollow(@Body body: AcceptOrRejectModel): Response<AddPostResponse>
    // response is like the same as addPostResponse and it just gets success and message

    @POST("auth/rejectRequest")
    suspend fun rejectFollow(@Body body: AcceptOrRejectModel) : Response<AddPostResponse>
    // response is like the same as addPostResponse and it just gets success and message

    @POST("post/like")
    suspend fun likePost(@Body body: LikeCommentModel): Response<JustStringResponse>

    @POST("post/comment")
    suspend fun addComment(@Body body: LikeCommentModel): Response<CommentResponse>

    @GET("post/getHomePosts")
    suspend fun getHomePosts(@Query("page") page: Int, @Query("limit") limit: Int): Response<HomePostsResponse>

    @GET("post/getAllPosts")
    suspend fun getSearchPosts(@Query("page") page: Int, @Query("limit") limit: Int): Response<HomePostsResponse>

    @GET("user/notifications")
    suspend fun getNotifications(): Response<NotificationResponse>

    @GET("post/getPost/{postId}")
    suspend fun getOnePost(@Path("postId") postId: String): Response<Post>
}
