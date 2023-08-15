package com.malkinfo.janataaapp.models.base

import com.google.gson.annotations.SerializedName

class FileUploadItem (
    @SerializedName("fileurl")
    var fileurl: String? = null,
    @SerializedName("type")
    var type: String ?=null
)
