package com.malkinfo.janataaapp.models.community

import com.google.gson.annotations.SerializedName

class ReportModel(
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("user_id")
    var user_id: String? = null,
    @SerializedName("reason")
    var reason: String? = null,
    @SerializedName("comment")
    var comment: String? = null,
    @SerializedName("post_id")
    var post_id: String? = null
)
