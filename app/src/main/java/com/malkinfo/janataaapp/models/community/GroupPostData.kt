package com.malkinfo.janataaapp.models.community

import com.google.gson.annotations.SerializedName

class GroupPostData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("about")
    var about: String ? = null,
    @SerializedName("groups")
    var groups: ArrayList<GroupSuggestionModel>? = null,
    @SerializedName("members")
    var members: ArrayList<GroupMemberModel>? = null,
    @SerializedName("posts")
    var posts: ArrayList<GroupPostModel>? = null,
    @SerializedName("total_count")
    var total_count : Int ?= null,
)