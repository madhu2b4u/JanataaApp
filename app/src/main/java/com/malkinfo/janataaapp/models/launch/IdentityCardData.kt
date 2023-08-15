package com.malkinfo.janataaapp.models.launch

import com.google.gson.annotations.SerializedName

class IdentityCardData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("user")
    var user: User? = null
)
