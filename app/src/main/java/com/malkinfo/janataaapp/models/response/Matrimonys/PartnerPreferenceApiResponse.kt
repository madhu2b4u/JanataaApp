package com.malkinfo.janataaapp.models.response.Matrimonys

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.Matrimony.PartnerPreferenceData

import java.lang.reflect.Type

class PartnerPreferenceApiResponse {
    var partnerPreferenceData: PartnerPreferenceData? = null
    class PartnerPreferenceApiDeserializer  :
        JsonDeserializer<PartnerPreferenceApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): PartnerPreferenceApiResponse {
            val partnerpreferenceapi = PartnerPreferenceApiResponse()
            val jsonObject = json!!.asJsonObject

            partnerpreferenceapi.partnerPreferenceData = Gson().fromJson(jsonObject, PartnerPreferenceData::class.java)

            return partnerpreferenceapi
        }

    }
}