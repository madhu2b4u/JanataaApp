package com.malkinfo.janataaapp.models.launch

import com.google.gson.annotations.SerializedName

class SignUpData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("user")
    var user: User?= null
)
