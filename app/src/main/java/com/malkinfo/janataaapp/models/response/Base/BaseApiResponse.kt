package com.malkinfo.janataaapp.models.response.Base

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class BaseApiResponse {
    var baseData: BaseData? = null

    class BaseApiDeserializer : JsonDeserializer<BaseApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): BaseApiResponse {
            val baseapi = BaseApiResponse()
            val jsonObject = json!!.asJsonObject

            baseapi.baseData = Gson().fromJson(jsonObject,BaseData::class.java)

            return baseapi
        }

    }
}