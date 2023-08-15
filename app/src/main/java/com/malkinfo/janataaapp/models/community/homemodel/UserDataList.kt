package com.malkinfo.janataaapp.models.community.homemodel

import com.google.gson.annotations.SerializedName
import com.malkinfo.janataaapp.models.launch.User

class UserDataList(
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("users")
    var users :ArrayList<User> ? = null,
    @SerializedName("total_count")
    var total_count : Int ?= null
) {

}