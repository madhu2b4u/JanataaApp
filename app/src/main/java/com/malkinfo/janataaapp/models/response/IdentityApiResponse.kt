package com.malkinfo.janataaapp.models.response

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.launch.IdentityCardData
import java.lang.reflect.Type

class IdentityApiResponse {
    var identityCardData: IdentityCardData? = null

    class IdentityCardApiDeserializer : JsonDeserializer<IdentityApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): IdentityApiResponse {
            val identitycardapi = IdentityApiResponse()
            val jsonObject = json!!.asJsonObject

            identitycardapi.identityCardData = Gson().fromJson(jsonObject, IdentityCardData::class.java)

            return identitycardapi
        }

    }
}
