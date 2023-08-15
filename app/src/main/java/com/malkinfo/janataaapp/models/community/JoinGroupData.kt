package com.malkinfo.janataaapp.models.community

import com.google.gson.annotations.SerializedName

class JoinGroupData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("message")
    var message: String? = null
)