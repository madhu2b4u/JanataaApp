package com.malkinfo.janataaapp.models.response.Community

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.community.WritePostData
import java.lang.reflect.Type

class WritePostApiResponse {
    var writePostData: WritePostData? = null

    class writePostApiDeserializer  : JsonDeserializer<WritePostApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): WritePostApiResponse {
            val writepostapi = WritePostApiResponse()
            val jsonObject = json!!.asJsonObject

            writepostapi.writePostData = Gson().fromJson(jsonObject, WritePostData::class.java)

            return writepostapi
        }

    }
}
