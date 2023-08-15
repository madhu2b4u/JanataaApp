package com.malkinfo.janataaapp.models.response.Community

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.community.homemodel.HomeAllGroupPostData
import java.lang.reflect.Type

class HomeGroupPostApiResponse {
    var homeAllGroupPostData: HomeAllGroupPostData? = null

    class homeAllPostDeserializer : JsonDeserializer<HomeGroupPostApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): HomeGroupPostApiResponse {

            val homeallgrouppost = HomeGroupPostApiResponse()
            val jsonObject = json!!.asJsonObject

            homeallgrouppost.homeAllGroupPostData = Gson().fromJson(jsonObject, HomeAllGroupPostData::class.java)

            return homeallgrouppost
        }

    }
}