package com.malkinfo.janataaapp.models.response.Matrimonys

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.Matrimony.MessageData
import java.lang.reflect.Type

class MessageApiResponse {
    var messageData: MessageData? = null

    class MessageApiDeserializer  :
        JsonDeserializer<MessageApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): MessageApiResponse {
            val messageapi = MessageApiResponse()
            val jsonObject = json!!.asJsonObject

            messageapi.messageData = Gson().fromJson(jsonObject, MessageData::class.java)

            return messageapi
        }

    }
}