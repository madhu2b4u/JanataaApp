package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName

class MessageData(

    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("message")
    var message: SendMessageItem? = null
    )