package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName

class ChatListItem (
    @SerializedName("profile_url")
    var profile_url: String? = null,
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("full_name")
    var full_name: String? = null,
    @SerializedName("latest_message")
    var latest_message: MessageItem? = null,
    @SerializedName("new_message")
    var new_message : Int? = null

)