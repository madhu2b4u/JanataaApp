package com.malkinfo.janataaapp.models.response.Community

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.community.ViewPostData
import java.lang.reflect.Type

class ViewPostApiResponse {
    var viewPostData: ViewPostData? = null

    class ViewPostApiDeserializer  : JsonDeserializer<ViewPostApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): ViewPostApiResponse {
            val viewpostapi = ViewPostApiResponse()
            val jsonObject = json!!.asJsonObject

            viewpostapi.viewPostData = Gson().fromJson(jsonObject, ViewPostData::class.java)
            Log.d("mTag","viewPostData = ${viewpostapi.viewPostData!!.post} and sucsse = ${viewpostapi.viewPostData!!.success}")

            return viewpostapi
        }

    }
}