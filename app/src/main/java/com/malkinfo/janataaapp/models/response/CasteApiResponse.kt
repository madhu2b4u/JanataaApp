package com.malkinfo.janataaapp.models.response

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.base.CasteData
import java.lang.reflect.Type

class CasteApiResponse {
    var casteData: CasteData? = null

    class CasteApiDeserializer : JsonDeserializer<CasteApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): CasteApiResponse {
            val casteapi = CasteApiResponse()
            val jsonObject = json!!.asJsonObject

            casteapi.casteData = Gson().fromJson(jsonObject, CasteData::class.java)

            return casteapi
        }

    }
}
