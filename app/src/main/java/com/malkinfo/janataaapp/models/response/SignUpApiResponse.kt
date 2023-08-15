package com.malkinfo.janataaapp.models.response

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.launch.SignUpData

import java.lang.reflect.Type

class SignUpApiResponse {
    var signUpData: SignUpData? = null

    class SignUpApiDeserializer : JsonDeserializer<SignUpApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): SignUpApiResponse {
            val signupapi = SignUpApiResponse()
            val jsonObject = json!!.asJsonObject

            signupapi.signUpData = Gson().fromJson(jsonObject, SignUpData::class.java)

            return signupapi
        }

    }
}
