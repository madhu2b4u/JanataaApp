package com.malkinfo.janataaapp.models.response.Community

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.community.ProfilePostData
import java.lang.reflect.Type

class ProfilePostApiResponse {
    var profilePostData: ProfilePostData? = null

    class ProfilePostApiDeserializer : JsonDeserializer<ProfilePostApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): ProfilePostApiResponse {
            val profilePostapi = ProfilePostApiResponse()
            val jsonObject = json!!.asJsonObject

            profilePostapi.profilePostData = Gson().fromJson(jsonObject, ProfilePostData::class.java)

            return profilePostapi
        }

    }
}