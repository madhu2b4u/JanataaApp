package com.malkinfo.janataaapp.models.response

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.base.CountryData
import java.lang.reflect.Type

class CountryApiResponse {
    var countryData: CountryData? = null

    class CountryApiDeserializer : JsonDeserializer<CountryApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): CountryApiResponse {
            val countryapi = CountryApiResponse()
            val jsonObject = json!!.asJsonObject

            countryapi.countryData = Gson().fromJson(jsonObject, CountryData::class.java)

            return countryapi
        }

    }
}