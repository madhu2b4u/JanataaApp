package com.malkinfo.janataaapp.models.base

import com.google.gson.annotations.SerializedName

class StateItem (
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("state")
    var state: String? = null
)