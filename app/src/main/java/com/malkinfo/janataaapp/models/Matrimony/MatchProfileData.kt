package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName

class MatchProfileData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("user")
    var user: ArrayList<MatchProfileItem>? = null,
    @SerializedName("total_count")
    var total_count : Int ? =null
)