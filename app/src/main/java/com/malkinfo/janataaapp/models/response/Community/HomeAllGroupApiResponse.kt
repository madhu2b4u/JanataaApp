package com.malkinfo.janataaapp.models.response.Community

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.community.homemodel.HomeGroupData
import java.lang.reflect.Type

class HomeAllGroupApiResponse {
    var homeAllGroupData: HomeGroupData? = null

    class HomeAllGroupApiDeserializer  : JsonDeserializer<HomeAllGroupApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): HomeAllGroupApiResponse {
            val homegroupapi = HomeAllGroupApiResponse()
            val jsonObject = json!!.asJsonObject
            homegroupapi.homeAllGroupData = Gson().fromJson(jsonObject, HomeGroupData::class.java)
            return homegroupapi
        }

    }
}