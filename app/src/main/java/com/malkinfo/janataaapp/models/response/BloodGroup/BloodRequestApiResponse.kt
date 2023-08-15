package com.malkinfo.janataaapp.models.response.BloodGroup

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.bloodgroup.BloodRequestData
import java.lang.reflect.Type

class BloodRequestApiResponse {
    var bloodRequestData: BloodRequestData? = null

    class BloodRequestApiDeserializer : JsonDeserializer<BloodRequestApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): BloodRequestApiResponse {
            val bloodrequestapi = BloodRequestApiResponse()
            val jsonObject = json!!.asJsonObject

            bloodrequestapi.bloodRequestData = Gson().fromJson(jsonObject, BloodRequestData::class.java)

            return bloodrequestapi
        }

    }
}