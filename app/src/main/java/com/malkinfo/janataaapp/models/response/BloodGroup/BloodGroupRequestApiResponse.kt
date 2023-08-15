package com.malkinfo.janataaapp.models.response.BloodGroup

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.bloodgroup.BloodGroupRequestData
import java.lang.reflect.Type

class BloodGroupRequestApiResponse {
    var bloodGroupRequestData: BloodGroupRequestData? = null

    class BloodGroupApiDeserializer : JsonDeserializer<BloodGroupRequestApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): BloodGroupRequestApiResponse {
            val bloodgrouprequestapi = BloodGroupRequestApiResponse()
            val jsonObject = json!!.asJsonObject

            bloodgrouprequestapi.bloodGroupRequestData = Gson().fromJson(jsonObject, BloodGroupRequestData::class.java)

            return bloodgrouprequestapi
        }

    }
}