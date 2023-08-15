package com.malkinfo.janataaapp.models.base

import com.google.gson.annotations.SerializedName

class VillageData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("village")
    var village: ArrayList<VillageItem>? = null
)


