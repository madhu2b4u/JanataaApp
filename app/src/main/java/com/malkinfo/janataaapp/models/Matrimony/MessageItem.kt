package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName

class MessageItem (

    @SerializedName("my_message")
    var my_message: Boolean? = null,

    @SerializedName("content_type")
    var content_type: String? = null,

    @SerializedName("seen")
    var seen: Boolean? = null,

    @SerializedName("_id")
    var _id: String? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("message_to")
    var message_to: MessagerItem? = null,

    @SerializedName("user_id")
    var user_id: MessagerItem? = null,

    @SerializedName("sent_on")
    var sent_on: String? = null,

    @SerializedName("__v")
    var __v: Int? = null

)