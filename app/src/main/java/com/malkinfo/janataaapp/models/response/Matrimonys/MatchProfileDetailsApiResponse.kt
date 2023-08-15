package com.malkinfo.janataaapp.models.response.Matrimonys

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.Matrimony.MatchProfileDetailsData
import java.lang.reflect.Type

class MatchProfileDetailsApiResponse {
    var matchProfileDetailsData: MatchProfileDetailsData? = null

    class MatchProfileDetailsApiDeserializer  :
        JsonDeserializer<MatchProfileDetailsApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): MatchProfileDetailsApiResponse {
            val matchprofiledetailsapi = MatchProfileDetailsApiResponse()
            val jsonObject = json!!.asJsonObject

            matchprofiledetailsapi.matchProfileDetailsData = Gson().fromJson(jsonObject, MatchProfileDetailsData::class.java)

            return matchprofiledetailsapi
        }

    }
}