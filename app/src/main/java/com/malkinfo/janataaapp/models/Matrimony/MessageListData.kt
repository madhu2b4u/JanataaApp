package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName

class MessageListData (

    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("messages")
    var messages: ArrayList<MessageListItem>? = null,
    @SerializedName("total_count")
    var total_count: Int? =null
)
