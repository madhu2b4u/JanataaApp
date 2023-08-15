package com.malkinfo.janataaapp.models.launch

import com.google.gson.annotations.SerializedName
import com.malkinfo.janataaapp.models.Matrimony.PartnerPrefereneItem

class LoginData(

    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("user_id")
    var user_id: String? = null,
    @SerializedName("user")
    var user: User? = null,
    @SerializedName("partner_preference")
    var  partner_preference: PartnerPrefereneItem? = null,
    @SerializedName("existingUser")
    var existingUser: Boolean? = null,
    @SerializedName("otp_id")
    var otp_id: String? = null,
    @SerializedName("token")
    var token: String? = null,
    @SerializedName("refreshToken")
    var refreshToken: String? = null,
    @SerializedName("message")
    var message: String? = null
)