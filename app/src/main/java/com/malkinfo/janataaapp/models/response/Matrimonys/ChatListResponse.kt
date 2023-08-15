package com.malkinfo.janataaapp.models.response.Matrimonys

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.Matrimony.ChatListData
import java.lang.reflect.Type

class ChatListResponse {
    var chatListData: ChatListData? = null

    class ChatListApiDeserializer  :
        JsonDeserializer<ChatListResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): ChatListResponse {
            val chatlistapi = ChatListResponse()
            val jsonObject = json!!.asJsonObject

            chatlistapi.chatListData = Gson().fromJson(jsonObject, ChatListData::class.java)

            return chatlistapi
        }

    }
}