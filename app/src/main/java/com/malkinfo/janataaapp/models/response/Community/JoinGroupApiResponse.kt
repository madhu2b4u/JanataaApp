package com.malkinfo.janataaapp.models.response.Community

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.community.JoinGroupData
import java.lang.reflect.Type

class JoinGroupApiResponse {
    var joinGroupData: JoinGroupData? = null

    class JoinGroupApiDeserializer : JsonDeserializer<JoinGroupApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): JoinGroupApiResponse {
            val joingroupapi = JoinGroupApiResponse()
            val jsonObject = json!!.asJsonObject

            joingroupapi.joinGroupData = Gson().fromJson(jsonObject, JoinGroupData::class.java)

            return joingroupapi
        }

    }
}