package com.malkinfo.janataaapp.models.community.homemodel

import com.google.gson.annotations.SerializedName
import com.malkinfo.janataaapp.models.community.GroupModel

class HomeGroupData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("groups")
    var groups: ArrayList<GroupModel>? = null,
    @SerializedName("total_count")
    var total_count : Int ?= null
)