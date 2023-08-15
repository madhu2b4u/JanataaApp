package com.malkinfo.janataaapp.models.community

import com.google.gson.annotations.SerializedName

class ViewPostData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("post")
    var post: GroupPostModel? = null
)