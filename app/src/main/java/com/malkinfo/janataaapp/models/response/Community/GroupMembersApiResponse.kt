package com.malkinfo.janataaapp.models.response.Community

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.community.GroupMembersData
import java.lang.reflect.Type

class GroupMembersApiResponse {
    var groupMembersData: GroupMembersData? = null

    class AboutGroupApiDeserializer  : JsonDeserializer<GroupMembersApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): GroupMembersApiResponse {
            val aboutgroupapi = GroupMembersApiResponse()
            val jsonObject = json!!.asJsonObject

            aboutgroupapi.groupMembersData = Gson().fromJson(jsonObject, GroupMembersData::class.java)

            return aboutgroupapi
        }

    }
}