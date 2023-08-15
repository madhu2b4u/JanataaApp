package com.malkinfo.janataaapp.models.base

import com.google.gson.annotations.SerializedName
import com.malkinfo.janataaapp.models.Matrimony.CountryItem

class CountryData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("country")
    var country: ArrayList<CountryItem>? = null,
    @SerializedName("total_count")
    var total_count : Int? = null
)