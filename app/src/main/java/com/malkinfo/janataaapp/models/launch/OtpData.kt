package com.malkinfo.janataaapp.models.launch

import com.google.gson.annotations.SerializedName

class OtpData(
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("error")
    var error: String? = null
)
