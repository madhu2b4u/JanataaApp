package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName

class StarItem (
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("star")
    var star: String? = null,
    @SerializedName("__v")
    var __v: String? = null
)