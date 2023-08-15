package com.malkinfo.janataaapp.models.response

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.launch.FeatureEnableData
import java.lang.reflect.Type

class FeatureEnableApiResponse {
    var featureEnableData: FeatureEnableData? = null

    class FeatureEnableApiDeserializer : JsonDeserializer<FeatureEnableApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): FeatureEnableApiResponse {
            val featureenableapi = FeatureEnableApiResponse()
            val jsonObject = json!!.asJsonObject

            featureenableapi.featureEnableData = Gson().fromJson(jsonObject, FeatureEnableData::class.java)

            return featureenableapi
        }

    }
}
