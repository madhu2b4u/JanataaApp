package com.malkinfo.janataaapp.managers.retrofit

import com.google.gson.JsonObject
import com.malkinfo.janataaapp.models.response.Base.BaseApiResponse
import com.malkinfo.janataaapp.models.response.Matrimonys.*
import com.malkinfo.janataaapp.models.response.StateApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface MatrimonyApi {
    @GET("user/matrimony")
    fun getMatrimonyRegisterDetails(): Deferred<Response<MatrimonyRegistrationDetailsApiResponse>>

    @GET("city")
    fun getCity(@Query("state") state : String) : Deferred<Response<CityApiResponse>>

    @GET("state")
    fun getState(@Query ("country") country : String): Deferred<Response<StateApiResponse>>

    @PUT("matrimonyuser")
    fun doMatrimonyRegistration(@Body gsonObject : JsonObject) : Deferred<Response<MatrimonyUserApiResponse>>

    @POST("partner")
    fun doPartnerPreference(@Body gsonObject: JsonObject) : Deferred<Response<PartnerPreferenceApiResponse>>

    @PUT("partner/{id}")
    fun doUpdatePartnerPreference(@Path("id") id : String,@Body gsonObject: JsonObject) : Deferred<Response<PartnerPreferenceApiResponse>>

    @GET("discover")
    fun getMatchProfile(@Query("page") page : String , @Query("limit") limit : String): Deferred<Response<MatchProfileApiResponse>>

    @GET("discover")
    fun getMatchProfileFilter(@Query("partner_id") partner_id : String,@Query ("filter") filter : String, @Query("page") page : String, @Query("limit") limit : String): Deferred<Response<MatchProfileApiResponse>>

    @POST("discover/{id}/shortlist")
    fun doShortlist(@Path("id") id : String): Deferred<Response<BaseApiResponse>>

    @GET("discover/{id}")
    fun getMatchProfileDetails(@Path("id") id : String) : Deferred<Response<MatchProfileDetailsApiResponse>>

    @POST("discover/{id}/block")
    fun doMatchProfileBlock(@Path("id") id : String) : Deferred<Response<BaseApiResponse>>

    @GET("discover/shortlist")
    fun getShortlist(): Deferred<Response<ShortlistProfileApiResponse>>

    @GET("discover/{id}/block")
    fun getBlock(@Path("id") id : String): Deferred<Response<BlockedProfileApiResponse>>


    @POST("matrimony-report")
    fun doReportMatrimonyProfile(@Body gsonObject: JsonObject) : Deferred<Response<BaseApiResponse>>


    @GET("matrimonyuser")
    fun getMatrimonyProfileDetails() : Deferred<Response<MatrimonyProfileApiResponse>>

    @POST("chat")
    fun doSendMessage(@Body gsonObject: JsonObject) : Deferred<Response<MessageApiResponse>>

    @GET("chat")
    fun getMessageList(@Query ("receiver_id") receiver_id : String,@Query("page") page : String, @Query("limit") limit : String) : Deferred<Response<MessageListApiResponse>>

    @GET("chat/chat-list")
    fun getChatList(): Deferred<Response<ChatListResponse>>

    @PUT("chat/clearchat")
    fun doClearChat(@Query ("receiver_id") receiver_id : String): Deferred<Response<BaseApiResponse>>

}