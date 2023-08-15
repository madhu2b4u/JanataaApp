package com.malkinfo.janataaapp.models.base

import com.google.gson.annotations.SerializedName

class FileUploadData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("media_file")
    var media_file: ArrayList<FileUploadItem>? = null

)
