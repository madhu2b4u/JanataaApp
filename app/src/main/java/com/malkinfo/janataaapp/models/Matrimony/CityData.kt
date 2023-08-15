package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName

class CityData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("cities")
    var cities: ArrayList<CityItem>? = null,
    @SerializedName("total_count")
    var  total_count : Int ?= null
)