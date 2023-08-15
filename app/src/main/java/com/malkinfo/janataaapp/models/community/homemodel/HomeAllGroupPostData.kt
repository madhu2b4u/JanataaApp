package com.malkinfo.janataaapp.models.community.homemodel

import com.google.gson.annotations.SerializedName
import com.malkinfo.janataaapp.models.community.GroupMemberModel
import com.malkinfo.janataaapp.models.community.GroupSuggestionModel
import com.malkinfo.janataaapp.models.launch.User

class HomeAllGroupPostData {
    @SerializedName("success")
    var success: Boolean? = null
    @SerializedName("about")
    var about: String ? = null
    @SerializedName("followersCount")
    var followersCount: Int? = null
    @SerializedName("user")
    var user: User? = null
    @SerializedName("groups")
    var groups: ArrayList<GroupSuggestionModel>? = null
    @SerializedName("members")
    var members: ArrayList<GroupMemberModel>? = null
    @SerializedName("posts")
    var posts: ArrayList<HomePostModel>? = null
    @SerializedName("total_count")
    var total_count : Int ?= null
}