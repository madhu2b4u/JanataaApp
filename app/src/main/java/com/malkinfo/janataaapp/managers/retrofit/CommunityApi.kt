package com.malkinfo.janataaapp.managers.retrofit

import com.google.gson.JsonObject
import com.malkinfo.janataaapp.models.response.Base.BaseApiResponse
import com.malkinfo.janataaapp.models.response.Community.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface CommunityApi {

    @GET("group")
    fun getGroup(@Query("page")  page : String , @Query("limit") limit : String) : Deferred<Response<GroupApiResponse>>

    @GET("user")
    fun getAllUser(): Deferred<Response<UserApiResponse>>

    @GET("group")
    fun getHomeAllGroup(@Query("page")  page : String , @Query("limit") limit : String) : Deferred<Response<HomeAllGroupApiResponse>>

    @POST("group/{id}/join")
    fun doJoinGroup(@Path("id") id: String):Deferred<Response<JoinGroupApiResponse>>

    @GET("group/{id}")
    fun doGetGroupPosts(@Path("id") id : String , @Query("page")  page : String , @Query("limit") limit : String): Deferred<Response<GroupPostApiResponse>>

    /**This is get home group*/
    @GET("group/{id}")
    fun doHomeGroupPost(@Path("id") id : String , @Query("page")  page : String , @Query("limit") limit : String): Deferred<Response<HomeGroupPostApiResponse>>


    @POST("post/{id}/like")
    fun doGroupPostLike(@Path("id") id: String): Deferred<Response<BaseApiResponse>>

    @GET("group/{id}/group_details")
    fun doGetAboutGroup(@Path("id") id : String, @Query("page")  page : String , @Query("limit") limit : String) : Deferred<Response<GroupMembersApiResponse>>

    @GET("post/{id}")
    fun doViewPost(@Path("id") id : String) : Deferred<Response<ViewPostApiResponse>>

    @GET("comment/{id}")
    fun doGetComments(@Path("id") id : String, @Query("page")  page : String , @Query("limit") limit : String  ) : Deferred<Response<CommentApiResponse>>

    @POST("comment")
    fun doAddComment(@Body gsonObject: JsonObject): Deferred<Response<AddCommentApiResponse>>

    @DELETE("comment/{id}")
    fun doDeleteComment(@Path("id") id : String): Deferred<Response<BaseApiResponse>>

    @POST("post/report/{id}")
    fun doReportPost(@Path("id") id : String,@Body gsonObject: JsonObject): Deferred<Response<ReportPostApiResponse>>

    @POST("post")
    fun doAddPost(@Body gsonObject: JsonObject) : Deferred<Response<WritePostApiResponse>>

    @PUT("post/{id}")
    fun doUpdatePost(@Path("id") id : String,@Body gsonObject: JsonObject) : Deferred<Response<WritePostApiResponse>>

    @GET("user/{id}")
    fun doGetProfilePosts(@Path("id") id : String, @Query("group_id") group_id: String, @Query("page")  page : String, @Query("limit") limit : String) : Deferred<Response<UserDetailsApiResponse>>

    @GET("user/{id}/followers")
    fun doGetFollowers(@Path("id") id : String, @Query("page")  page : String , @Query("limit") limit : String) : Deferred<Response<FollowerApiResponse>>

    @GET("search")
    fun doSearchFollowers(@Query("username") username : String,@Query("user_id") user_id : String,
                          @Query("search") search : String,@Query("group_id") group_id : String) : Deferred<Response<FollowerApiResponse>>

    @GET("search")
    fun doSearchMembers(@Query("username") username : String,
                        @Query("search") search : String,@Query("group_id") group_id : String) : Deferred<Response<GroupPostApiResponse>>

    @DELETE("post/{id}")
    fun doDeletePost(@Path("id") id : String): Deferred<Response<BaseApiResponse>>

    @POST("user/{id}/follow")
    fun doFollow(@Path("id") id : String) : Deferred<Response<BaseApiResponse>>

    @GET("group/myposts")
    fun doGetAllPost(@Query("group_id") group_id: String , @Query("user_id") user_id: String) : Deferred<Response<ProfilePostApiResponse>>

    @GET("meeting")
    fun doGetMeeting(@Query("group_id") group_id : String) : Deferred<Response<MeetingApiResponse>>


}