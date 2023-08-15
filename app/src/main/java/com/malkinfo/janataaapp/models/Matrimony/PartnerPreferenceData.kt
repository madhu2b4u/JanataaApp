package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName

class PartnerPreferenceData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("partnerPreference")
    var partnerPreference: PartnerPrefereneItem ?= null
)