package com.malkinfo.janataaapp.models.community

import com.google.gson.annotations.SerializedName
import com.malkinfo.janataaapp.models.launch.User

class UserDetailsData(
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("user")
    var user: User? = null,
    @SerializedName("followers")
    var followers: ArrayList<FollowerModel>? = null,
    @SerializedName("followersCount")
    var followersCount: Int? = null,
    @SerializedName("posts")
    var posts: ArrayList<ProfilePostModel>? = null,
    @SerializedName("total_count")
    var total_count : Int ? =null
)
