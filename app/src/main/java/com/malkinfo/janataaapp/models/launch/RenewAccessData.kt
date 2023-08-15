package com.malkinfo.janataaapp.models.launch

import com.google.gson.annotations.SerializedName

class RenewAccessData (

    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("user")
    var user: User? = null,
    @SerializedName("token")
    var token:String?=null,
    @SerializedName("message")
    var message : String?= null
)
