package com.malkinfo.janataaapp.models.base

import com.google.gson.annotations.SerializedName

class DistrictItem (
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("district")
    var district: String? = null
)