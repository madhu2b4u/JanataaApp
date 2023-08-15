package com.malkinfo.janataaapp.models.response.Community

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.community.GroupPostData
import java.lang.reflect.Type

class GroupPostApiResponse {
    var groupPostData: GroupPostData? = null

    class GroupPostApiDeserializer  : JsonDeserializer<GroupPostApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): GroupPostApiResponse {
            val grouppostapi = GroupPostApiResponse()
            val jsonObject = json!!.asJsonObject

            grouppostapi.groupPostData = Gson().fromJson(jsonObject, GroupPostData::class.java)

            return grouppostapi
        }

    }
}