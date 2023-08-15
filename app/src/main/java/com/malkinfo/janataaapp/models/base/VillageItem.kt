package com.malkinfo.janataaapp.models.base

import com.google.gson.annotations.SerializedName

class VillageItem (
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("village")
    var village: String? = null
)
