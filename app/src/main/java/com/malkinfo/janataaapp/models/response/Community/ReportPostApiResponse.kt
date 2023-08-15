package com.malkinfo.janataaapp.models.response.Community

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.community.ReportData
import java.lang.reflect.Type

class ReportPostApiResponse {
    var reportData: ReportData? = null

    class ReportPostApiDeserializer  : JsonDeserializer<ReportPostApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): ReportPostApiResponse {
            val reportapi = ReportPostApiResponse()
            val jsonObject = json!!.asJsonObject

            reportapi.reportData = Gson().fromJson(jsonObject, ReportData::class.java)

            return reportapi
        }

    }
}