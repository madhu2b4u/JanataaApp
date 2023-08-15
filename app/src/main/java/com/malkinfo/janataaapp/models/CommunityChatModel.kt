package com.malkinfo.janataaapp.models

import com.google.gson.annotations.SerializedName

class CommunityChatModel (
    @SerializedName("image")
    var image: Int? = null,
    @SerializedName("groupName")
    var groupName: String? = null,
    @SerializedName("totalGroupMembers")
    var totalGroupMembers: String? = null,
    @SerializedName("aboutGroup")
    var aboutGroup :String?=null,
    @SerializedName("isMute")
    var isMute :Boolean?=null,
    @SerializedName("isJoin")
    var isJoin :Boolean?=null
)