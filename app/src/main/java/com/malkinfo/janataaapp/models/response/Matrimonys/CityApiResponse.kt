package com.malkinfo.janataaapp.models.response.Matrimonys

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.Matrimony.CityData
import java.lang.reflect.Type

class CityApiResponse {
    var cityData: CityData? = null

    class CityApiDeserializer  :
        JsonDeserializer<CityApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): CityApiResponse {
            val cityapi = CityApiResponse()
            val jsonObject = json!!.asJsonObject

            cityapi.cityData = Gson().fromJson(jsonObject, CityData::class.java)

            return cityapi
        }

    }
}