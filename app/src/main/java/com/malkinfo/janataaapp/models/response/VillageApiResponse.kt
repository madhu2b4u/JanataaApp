package com.malkinfo.janataaapp.models.response

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.base.VillageData
import java.lang.reflect.Type

class VillageApiResponse {
    var villageData: VillageData? = null

    class VillageApiDeserializer : JsonDeserializer<VillageApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): VillageApiResponse {
            val villageapi = VillageApiResponse()
            val jsonObject = json!!.asJsonObject

            villageapi.villageData = Gson().fromJson(jsonObject, VillageData::class.java)

            return villageapi
        }

    }
}
