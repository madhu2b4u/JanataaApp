package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName

class BlockedProfileData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("blocked_list")
    var blocked_list: ArrayList<BlockedProfileItem>? = null
)