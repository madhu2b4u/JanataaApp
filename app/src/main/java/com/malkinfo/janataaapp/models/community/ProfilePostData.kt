package com.malkinfo.janataaapp.models.community

import com.google.gson.annotations.SerializedName

class ProfilePostData(
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("posts")
    var posts: ArrayList<ProfilePostModel>? = null,
    @SerializedName("total_count")
    var total_count: Int? = null

)