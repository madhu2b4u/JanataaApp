package com.malkinfo.janataaapp.models.bloodgroup

import com.google.gson.annotations.SerializedName

class BloodRequestData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("blood")
    var blood: BloodRequestItem? = null
)