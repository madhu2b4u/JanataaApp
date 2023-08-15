package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName

class EmploymentItem(
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("employed_in")
    var employed_in: String? = null,
    @SerializedName("__v")
    var __v: String? = null
)