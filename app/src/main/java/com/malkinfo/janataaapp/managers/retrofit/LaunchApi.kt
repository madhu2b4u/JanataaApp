package com.malkinfo.janataaapp.managers.retrofit

import com.google.gson.JsonObject
import com.malkinfo.janataaapp.models.response.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LaunchApi{

   @POST("user/login")
    fun doLogin(@Body gsonObject: JsonObject): Deferred<Response<LoginApiResponse>>

    @POST("user/renewaccess")
    fun doRenewAccess(@Body gsonObject: JsonObject): Deferred<Response<RenewAccessApiResponse>>

    @POST("user/validate")
    fun doValidateOtp(@Body gsonObject: JsonObject): Deferred<Response<OtpApiResponse>>


}