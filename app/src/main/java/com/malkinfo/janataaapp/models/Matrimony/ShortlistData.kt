package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName

class ShortlistData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("shortlisted_user")
    var shortlisted_user: ArrayList<ShortlistProfileItem>? = null
)