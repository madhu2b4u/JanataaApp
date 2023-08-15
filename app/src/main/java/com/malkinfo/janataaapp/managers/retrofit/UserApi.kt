package com.malkinfo.janataaapp.managers.retrofit

import com.google.gson.JsonObject
import com.malkinfo.janataaapp.models.response.FeatureEnableApiResponse
import com.malkinfo.janataaapp.models.response.IdentityApiResponse
import com.malkinfo.janataaapp.models.response.SignUpApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserApi {
    @PUT("user")
    fun doSignUp(@Body gsonObject: JsonObject): Deferred<Response<SignUpApiResponse>>

    @GET("user/identitycard")
    fun getIdentityCard(): Deferred<Response<IdentityApiResponse>>

    @PUT("user/feature")
    fun doFeatureEnable(@Body gsonObject: JsonObject): Deferred<Response<FeatureEnableApiResponse>>


}