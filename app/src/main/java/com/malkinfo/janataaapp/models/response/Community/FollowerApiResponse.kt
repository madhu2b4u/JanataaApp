package com.malkinfo.janataaapp.models.response.Community

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.community.FollowerData
import java.lang.reflect.Type

class FollowerApiResponse {
    var followerData: FollowerData? = null

    class FollowerApiDeserializer  : JsonDeserializer<FollowerApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): FollowerApiResponse {
            val followerapi = FollowerApiResponse()
            val jsonObject = json!!.asJsonObject

            followerapi.followerData = Gson().fromJson(jsonObject, FollowerData::class.java)

            return followerapi
        }

    }
}
