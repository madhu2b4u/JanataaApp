package com.malkinfo.janataaapp.models.community

import com.google.gson.annotations.SerializedName

class GroupMemberModel (
    @SerializedName("_id")
    var _id : String ?=null,
    @SerializedName("full_name")
    var  full_name: String?=null,
    @SerializedName("profile_url")
    var profile_url: String?=null

)