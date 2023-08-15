package com.malkinfo.janataaapp.models.community

import com.google.gson.annotations.SerializedName
import com.malkinfo.janataaapp.models.launch.User

class CommentModel (
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("comment")
    var comment: String? = null,
    @SerializedName("post_id")
    var post_id: String? = null,
    @SerializedName("user_id")
    var user: User? = null,
    @SerializedName("commented_at")
    var commented_at: String? = null,
    @SerializedName("__v")
    var __v: Int? = null

    )