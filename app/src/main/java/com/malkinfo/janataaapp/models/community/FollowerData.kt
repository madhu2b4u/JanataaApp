package com.malkinfo.janataaapp.models.community

import com.google.gson.annotations.SerializedName

class FollowerData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("followers")
    var followers: ArrayList<FollowerModel>? = null,
    @SerializedName("followers_count")
    var followers_count : Int ?=null
)