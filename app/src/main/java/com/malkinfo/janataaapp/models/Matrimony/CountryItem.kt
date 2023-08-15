package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName

class CountryItem (
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("country")
    var country: String? = null,
    @SerializedName("__v")
    var __v: String? = null
)