package com.malkinfo.janataaapp.models.community

import com.google.gson.annotations.SerializedName
import com.malkinfo.janataaapp.models.launch.User

class WritePostModel (
    @SerializedName("_id")
    var _id :String?=null,
    @SerializedName("description")
    var description :String?=null,
    @SerializedName("user_id")
    var user_id : User?=null,
    @SerializedName("image_url")
    var image_url :String?=null,
    @SerializedName("published_at")
    var published_at :String?=null
)
