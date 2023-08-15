package com.malkinfo.janataaapp.models.response

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.launch.OtpData
import java.lang.reflect.Type

class OtpApiResponse {
    var otpData: OtpData? = null

    class OtpApiDeserializer : JsonDeserializer<OtpApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): OtpApiResponse {
            val otpapi = OtpApiResponse()
            val jsonObject = json!!.asJsonObject

            otpapi.otpData = Gson().fromJson(jsonObject, OtpData::class.java)

            return otpapi
        }

    }
}
