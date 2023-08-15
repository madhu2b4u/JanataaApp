package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName

class MatrimonyUserData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("matrimony")
    var matrimony: MatrimonyUserItem? = null
)