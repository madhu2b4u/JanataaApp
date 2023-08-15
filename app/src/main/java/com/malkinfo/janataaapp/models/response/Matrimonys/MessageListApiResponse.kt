package com.malkinfo.janataaapp.models.response.Matrimonys

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.Matrimony.MessageListData
import java.lang.reflect.Type

class MessageListApiResponse {
    var messageListData: MessageListData? = null

    class MessageListApiDeserializer  :
        JsonDeserializer<MessageListApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): MessageListApiResponse {
            val messagelistapi = MessageListApiResponse()
            val jsonObject = json!!.asJsonObject

            messagelistapi.messageListData = Gson().fromJson(jsonObject, MessageListData::class.java)

            return messagelistapi
        }

    }
}