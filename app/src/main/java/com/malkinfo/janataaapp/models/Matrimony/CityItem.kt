package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName

class CityItem (
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("city")
    var city: String? = null,
    @SerializedName("__v")
    var __v: String? = null
)