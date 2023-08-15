package com.malkinfo.janataaapp.models.response.Community

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.response.Base.BaseData
import java.lang.reflect.Type

class AddCommentApiResponse {
    var addCommentData: BaseData? = null

    class AddCommentApiDeserializer  : JsonDeserializer<AddCommentApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): AddCommentApiResponse {
            val addcommentapi = AddCommentApiResponse()
            val jsonObject = json!!.asJsonObject

            addcommentapi.addCommentData = Gson().fromJson(jsonObject, BaseData::class.java)

            return addcommentapi
        }

    }
}