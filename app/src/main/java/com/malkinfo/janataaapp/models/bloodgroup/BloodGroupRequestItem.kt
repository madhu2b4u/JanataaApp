package com.malkinfo.janataaapp.models.bloodgroup

import com.google.gson.annotations.SerializedName
import com.malkinfo.janataaapp.models.launch.User

class BloodGroupRequestItem(
    @SerializedName("my_request")
    var my_request: Boolean? = true,
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("blood")
    var blood: String? = null,
    @SerializedName("user_id")
    var user_id: User? = null,
    @SerializedName("location")
    var location: String? = null,
    @SerializedName("mobile")
    var mobile: String? = null,
    @SerializedName("requested_at")
    var requested_at: String? = null,
    @SerializedName("__v")
    var __v: Int? = null
)