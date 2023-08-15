package com.malkinfo.janataaapp.models.response

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.base.MandalData
import java.lang.reflect.Type

class MandalApiResponse {
    var mandalData: MandalData? = null

    class MandalApiDeserializer : JsonDeserializer<MandalApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): MandalApiResponse {
            val mandalapi = MandalApiResponse()
            val jsonObject = json!!.asJsonObject

            mandalapi.mandalData = Gson().fromJson(jsonObject, MandalData::class.java)

            return mandalapi
        }

    }
}