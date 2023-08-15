package com.malkinfo.janataaapp.models.response.Matrimonys

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.Matrimony.MatchProfileData
import java.lang.reflect.Type

class MatchProfileApiResponse {
    var matchProfileData: MatchProfileData? = null

    class MatchProfileApiDeserializer  :
        JsonDeserializer<MatchProfileApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): MatchProfileApiResponse {
            val matchprofileapi = MatchProfileApiResponse()
            val jsonObject = json!!.asJsonObject

            matchprofileapi.matchProfileData = Gson().fromJson(jsonObject, MatchProfileData::class.java)

            return matchprofileapi
        }

    }
}