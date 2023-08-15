package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName

class ChatListData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("users")
    var users: ArrayList<ChatListItem>? = null
)