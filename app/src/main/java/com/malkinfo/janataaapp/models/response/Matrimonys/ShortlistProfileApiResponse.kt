package com.malkinfo.janataaapp.models.response.Matrimonys

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.Matrimony.ShortlistData
import java.lang.reflect.Type

class ShortlistProfileApiResponse {
    var shortlistProfileData: ShortlistData? = null
    class ShortlistProfileApiDeserializer  :
        JsonDeserializer<ShortlistProfileApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): ShortlistProfileApiResponse {
            val shortlistprofileapi = ShortlistProfileApiResponse()
            val jsonObject = json!!.asJsonObject

            shortlistprofileapi.shortlistProfileData = Gson().fromJson(jsonObject, ShortlistData::class.java)

            return shortlistprofileapi
        }

    }
}