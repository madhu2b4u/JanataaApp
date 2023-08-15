package com.malkinfo.janataaapp.models.response

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.launch.LoginData
import java.lang.reflect.Type

class LoginApiResponse {
    var loginData: LoginData? = null

    class LoginApiDeserializer : JsonDeserializer<LoginApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): LoginApiResponse {
            val loginapi = LoginApiResponse()
            val jsonObject = json!!.asJsonObject

            loginapi.loginData = Gson().fromJson(jsonObject,LoginData::class.java)


            return loginapi
        }

    }
}