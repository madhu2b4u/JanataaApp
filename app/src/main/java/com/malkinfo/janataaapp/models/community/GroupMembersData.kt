package com.malkinfo.janataaapp.models.community

import com.google.gson.annotations.SerializedName

class GroupMembersData(
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("members")
    var members: ArrayList<GroupMemberModel>? = null,
    @SerializedName("total_count")
    var total_count: Int? = null
)
