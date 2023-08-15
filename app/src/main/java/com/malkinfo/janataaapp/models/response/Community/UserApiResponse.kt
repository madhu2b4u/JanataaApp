package com.malkinfo.janataaapp.models.response.Community

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.community.homemodel.UserDataList
import java.lang.reflect.Type

class UserApiResponse {
    var userDataList: UserDataList? = null

    class UserApiDeserializer  : JsonDeserializer<UserApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): UserApiResponse {
            val userapi = UserApiResponse()
            val jsonObject = json!!.asJsonObject
            userapi.userDataList = Gson().fromJson(jsonObject, UserDataList::class.java)
            return userapi
        }

    }
}