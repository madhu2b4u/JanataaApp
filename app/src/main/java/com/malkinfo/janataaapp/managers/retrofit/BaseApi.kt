package com.malkinfo.janataaapp.managers.retrofit

import com.google.gson.JsonObject
import com.malkinfo.janataaapp.models.response.*
import com.malkinfo.janataaapp.models.response.Base.BaseApiResponse
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface BaseApi {
    @GET("country")
    fun getCountry(): Deferred<Response<CountryApiResponse>>

    @GET("state")
    fun getState(@Query("country") country: String):Deferred<Response<StateApiResponse>>

    @GET("district")
    fun getDistrict(@Query("state") stateId: String): Deferred<Response<DistrictApiResponse>>

    @GET("mandal")
    fun getMandalList(@Query("district") districtId : String): Deferred<Response<MandalApiResponse>>

    @GET("village")
    fun getVillage(@Query("mandal") mandalId: String): Deferred<Response<VillageApiResponse>>

    @GET("caste/?page=&limit=?")
    fun getCaste(): Deferred<Response<CasteApiResponse>>


    @Multipart
    @POST("fileupload")
    fun fileUpload(@Part file : MultipartBody.Part?): Deferred<Response<FileUploadApiResponse>>

    @DELETE("user/{id}")
    fun doDeleteUserBYId(@Path("id") id : String): Deferred<Response<BaseApiResponse>>

    @PUT("user/deactivate")
    fun doDeactivate(@Query("id") userId: String,@Body gsonObject : JsonObject): Deferred<Response<BaseApiResponse>>


    @Multipart
    @POST("fileupload")
    fun multipleFileUpload(@Part file : Array<MultipartBody.Part?>): Deferred<Response<FileUploadApiResponse>>
}