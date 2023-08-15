package com.malkinfo.janataaapp.models.response.Community

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.community.GroupData
import java.lang.reflect.Type

class GroupApiResponse {
    var groupData: GroupData? = null

    class GroupApiDeserializer  : JsonDeserializer<GroupApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): GroupApiResponse {
            val groupapi = GroupApiResponse()
            val jsonObject = json!!.asJsonObject
            groupapi.groupData = Gson().fromJson(jsonObject, GroupData::class.java)
            return groupapi
        }

    }
}