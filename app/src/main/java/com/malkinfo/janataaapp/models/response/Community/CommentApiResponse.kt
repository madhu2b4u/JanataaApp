package com.malkinfo.janataaapp.models.response.Community

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.community.CommentData
import java.lang.reflect.Type

class CommentApiResponse {
    var commentData: CommentData? = null

    class CommentApiDeserializer  : JsonDeserializer<CommentApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): CommentApiResponse {
            val commentapi = CommentApiResponse()
            val jsonObject = json!!.asJsonObject

            commentapi.commentData = Gson().fromJson(jsonObject, CommentData::class.java)

            return commentapi
        }

    }
}