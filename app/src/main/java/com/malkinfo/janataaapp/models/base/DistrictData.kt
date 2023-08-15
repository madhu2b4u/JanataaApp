package com.malkinfo.janataaapp.models.base

import com.google.gson.annotations.SerializedName

class DistrictData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("district")
    var district: ArrayList<DistrictItem>? = null
)