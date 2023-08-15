package com.malkinfo.janataaapp.models.response.Matrimonys

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.Matrimony.MatrimonyUserData

import java.lang.reflect.Type

class MatrimonyUserApiResponse {
    var matrimonyUserDetailsData: MatrimonyUserData? = null

    class MatrimonyUserDetailsApiDeserializer  :
        JsonDeserializer<MatrimonyUserApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): MatrimonyUserApiResponse {
            val matrimonyuserdetailsapi = MatrimonyUserApiResponse()
            val jsonObject = json!!.asJsonObject

            matrimonyuserdetailsapi.matrimonyUserDetailsData = Gson().fromJson(jsonObject, MatrimonyUserData::class.java)

            return matrimonyuserdetailsapi
        }

    }
}