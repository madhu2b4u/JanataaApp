package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName


class MatrimonyProfileDetailsData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("matrimony")
    var matrimony: MatrimonyProfileItem? = null
)