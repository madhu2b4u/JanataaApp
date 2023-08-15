package com.malkinfo.janataaapp.models.response.Base

import com.google.gson.annotations.SerializedName

class BaseData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("message")
    var message: String? = null
)