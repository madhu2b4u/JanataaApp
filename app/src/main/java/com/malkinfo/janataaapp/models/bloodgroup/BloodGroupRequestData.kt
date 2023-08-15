package com.malkinfo.janataaapp.models.bloodgroup

import com.google.gson.annotations.SerializedName

class BloodGroupRequestData(
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("myBloodRequest")
    var myBloodRequest: ArrayList<BloodGroupRequestItem>? = null,
    @SerializedName("blood")
    var blood: ArrayList<BloodGroupRequestItem>? = null,
    @SerializedName("total_count")
    var total_count : Int? =null
)