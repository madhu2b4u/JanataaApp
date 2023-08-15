package com.malkinfo.janataaapp.models.response.Matrimonys

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.Matrimony.BlockedProfileData

import java.lang.reflect.Type

class BlockedProfileApiResponse {
    var blockedProfileData: BlockedProfileData? = null
    class BlockedProfileApiDeserializer  :
        JsonDeserializer<BlockedProfileApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): BlockedProfileApiResponse {
            val blockedprofileapi = BlockedProfileApiResponse()
            val jsonObject = json!!.asJsonObject

            blockedprofileapi.blockedProfileData = Gson().fromJson(jsonObject, BlockedProfileData::class.java)

            return blockedprofileapi
        }

    }
}