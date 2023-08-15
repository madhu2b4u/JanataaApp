package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName

class MoonItem (
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("moon_sign")
    var moon_sign: String? = null,
    @SerializedName("__v")
    var __v: String? = null
)