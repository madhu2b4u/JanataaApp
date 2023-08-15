package com.malkinfo.janataaapp.models.base

import com.google.gson.annotations.SerializedName

class MandalData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("mandals")
    var mandals: ArrayList<MandalItem>? = null,
    @SerializedName("total_count")
    var total_count : Int? = null
)