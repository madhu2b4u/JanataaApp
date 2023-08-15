package com.malkinfo.janataaapp.managers.retrofit

import com.google.gson.JsonObject
import com.malkinfo.janataaapp.models.response.Base.BaseApiResponse
import com.malkinfo.janataaapp.models.response.BloodGroup.BloodGroupRequestApiResponse
import com.malkinfo.janataaapp.models.response.BloodGroup.BloodRequestApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface BloodGroupApi {

    @POST("blood/filter")
    fun doGetBloodGroupRequest(@Query("page") page : String ,@Query("limit") limit : String): Deferred<Response<BloodGroupRequestApiResponse>>

    @POST("blood")
    fun doBloodRequest(@Body gsonObject: JsonObject) : Deferred<Response<BloodRequestApiResponse>>

    @POST("blood/filter")
   // @HTTP(method = "GET", path = "blood", hasBody = true)
    fun doFindBloodGroupRequest(@Body gsonObject: JsonObject) : Deferred<Response<BloodGroupRequestApiResponse>>

    @DELETE("blood/{id}")
    fun doDeleteMyBloodGroupRequest(@Path("id") id : String) : Deferred<Response<BaseApiResponse>>
}