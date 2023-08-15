package com.malkinfo.janataaapp.models.response.Community

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.community.UserDetailsData
import java.lang.reflect.Type

class UserDetailsApiResponse {
    var userDetailsData: UserDetailsData? = null

    class userDetailsApiDeserializer  : JsonDeserializer<UserDetailsApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): UserDetailsApiResponse {
            val userdetailsapi = UserDetailsApiResponse()
            val jsonObject = json!!.asJsonObject

            userdetailsapi.userDetailsData = Gson().fromJson(jsonObject, UserDetailsData::class.java)

            return userdetailsapi
        }

    }
}