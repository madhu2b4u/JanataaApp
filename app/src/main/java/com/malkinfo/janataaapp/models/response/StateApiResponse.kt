package com.malkinfo.janataaapp.models.response

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.base.StateData
import java.lang.reflect.Type

class StateApiResponse {
    var stateData: StateData? = null

    class StateApiDeserializer : JsonDeserializer<StateApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): StateApiResponse {
            val stateapi = StateApiResponse()
            val jsonObject = json!!.asJsonObject

            stateapi.stateData = Gson().fromJson(jsonObject, StateData::class.java)

            return stateapi
        }

    }
}