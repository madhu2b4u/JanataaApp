package com.malkinfo.janataaapp.models.response

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.malkinfo.janataaapp.models.base.FileUploadData
import java.lang.reflect.Type

class FileUploadApiResponse {
    var fileUploadData: FileUploadData? = null


    class FileUploadApiDeserializer : JsonDeserializer<FileUploadApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): FileUploadApiResponse {
            val fileuploadapi = FileUploadApiResponse()
            val jsonObject = json!!.asJsonObject

            fileuploadapi.fileUploadData = Gson().fromJson(jsonObject, FileUploadData::class.java)

            return fileuploadapi
        }

    }
}
