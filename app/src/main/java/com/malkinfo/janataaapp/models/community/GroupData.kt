package com.malkinfo.janataaapp.models.community

import com.google.gson.annotations.SerializedName


class GroupData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("groups")
    var groups: ArrayList<GroupModel>? = null,
    @SerializedName("total_count")
    var total_count : Int ?= null
)