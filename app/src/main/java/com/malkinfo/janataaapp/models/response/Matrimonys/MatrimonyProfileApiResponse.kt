package com.malkinfo.janataaapp.models.response.Matrimonys

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.Matrimony.MatrimonyProfileDetailsData
import java.lang.reflect.Type

class MatrimonyProfileApiResponse {
    var matrimonyProfileDetailsData: MatrimonyProfileDetailsData? = null

    class MatrimonyProfileDetailsApiDeserializer  :
        JsonDeserializer<MatrimonyProfileApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): MatrimonyProfileApiResponse {
            val matrimonyprofiledetailsapi = MatrimonyProfileApiResponse()
            val jsonObject = json!!.asJsonObject

            matrimonyprofiledetailsapi.matrimonyProfileDetailsData = Gson().fromJson(jsonObject, MatrimonyProfileDetailsData::class.java)

            return matrimonyprofiledetailsapi
        }

    }
}