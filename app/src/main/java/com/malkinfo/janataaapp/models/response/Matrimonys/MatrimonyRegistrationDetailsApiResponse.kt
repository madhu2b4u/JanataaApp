package com.malkinfo.janataaapp.models.response.Matrimonys

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.Matrimony.MatrimonyRegistrationDetailsData
import java.lang.reflect.Type

class MatrimonyRegistrationDetailsApiResponse {
    var matrimonyRegistrationDetailsData: MatrimonyRegistrationDetailsData? = null

    class MatrimonyRegistrationApiDeserializer  : JsonDeserializer<MatrimonyRegistrationDetailsApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): MatrimonyRegistrationDetailsApiResponse {
            val matrimonyregistrationapi = MatrimonyRegistrationDetailsApiResponse()
            val jsonObject = json!!.asJsonObject

            matrimonyregistrationapi.matrimonyRegistrationDetailsData = Gson().fromJson(jsonObject, MatrimonyRegistrationDetailsData::class.java)

            return matrimonyregistrationapi
        }

    }
}