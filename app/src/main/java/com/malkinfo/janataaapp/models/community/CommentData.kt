package com.malkinfo.janataaapp.models.community

import com.google.gson.annotations.SerializedName

class CommentData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("comments")
    var comments: ArrayList<CommentModel>? = null,
    @SerializedName("total_count")
    var total_count : Int ?= null
)