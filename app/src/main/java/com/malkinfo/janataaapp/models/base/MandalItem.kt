package com.malkinfo.janataaapp.models.base

import com.google.gson.annotations.SerializedName

class MandalItem (
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("mandal")
    var mandal: String? = null
)