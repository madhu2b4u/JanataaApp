package com.malkinfo.janataaapp.models.base

import com.google.gson.annotations.SerializedName

class StateData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("state")
    var state: ArrayList<StateItem>? = null,
    @SerializedName("total_count")
    var total_count : Int? = null
)