package com.malkinfo.janataaapp.models.response

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.base.DistrictData
import java.lang.reflect.Type

class DistrictApiResponse {
    var districtData: DistrictData? = null

    class DistrictApiDeserializer : JsonDeserializer<DistrictApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): DistrictApiResponse {
            val districtapi = DistrictApiResponse()
            val jsonObject = json!!.asJsonObject

            districtapi.districtData = Gson().fromJson(jsonObject, DistrictData::class.java)

            return districtapi
        }

    }
}
