package com.malkinfo.janataaapp.models.response.Community

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.community.MeetingData
import java.lang.reflect.Type

class MeetingApiResponse {
    var meetingData: MeetingData? = null

    class MeetingApiDeserializer  : JsonDeserializer<MeetingApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): MeetingApiResponse {
            val meetingapi = MeetingApiResponse()
            val jsonObject = json!!.asJsonObject

            meetingapi.meetingData = Gson().fromJson(jsonObject, MeetingData::class.java)

            return meetingapi
        }

    }
}