package com.malkinfo.janataaapp.models.community

import com.google.gson.annotations.SerializedName

class GroupSuggestionModel
    (
    @SerializedName("is_joined")
    var  is_joined: Boolean?=null,
    @SerializedName("profile_url")
    var profile_url: String?=null,
    @SerializedName("_id")
    var _id : String ?=null,
    @SerializedName("group")
    var  group : String ?= null
)