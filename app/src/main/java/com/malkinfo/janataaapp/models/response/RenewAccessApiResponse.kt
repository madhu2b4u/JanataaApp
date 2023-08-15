package com.malkinfo.janataaapp.models.response

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.launch.RenewAccessData
import java.lang.reflect.Type

class RenewAccessApiResponse {
    var renewAccessData: RenewAccessData? = null

    class RenewAccessApiDeserializer : JsonDeserializer<RenewAccessApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): RenewAccessApiResponse {
            val renewaccessapi = RenewAccessApiResponse()
            val jsonObject = json!!.asJsonObject

            renewaccessapi.renewAccessData = Gson().fromJson(jsonObject, RenewAccessData::class.java)

            return renewaccessapi
        }

    }
}