package com.malkinfo.janataaapp.models.community.homemodel

import com.google.gson.annotations.SerializedName
import com.malkinfo.janataaapp.models.launch.User


class HomePostModel (
    @SerializedName("image_url")
    var image_url: ArrayList<String>? = null,
    @SerializedName("likes_count")
    var likes_count: Int? = null,
    @SerializedName("views_count")
    var views_count: Int? = null,
    @SerializedName("is_liked")
    var is_liked :Boolean?=null,
    @SerializedName("comments_count")
    var comments_count :Int?=null,
    @SerializedName("_id")
    var _id :String?=null,
    @SerializedName("description")
    var description :String?=null,
    @SerializedName("user_id")
    var user_id : User?=null,
    @SerializedName("group_id")
    var group_id :ArrayList<String>?=null,
    @SerializedName("published_at")
    var published_at :String?=null,
    @SerializedName("__v")
    var __v :Int?=null

)