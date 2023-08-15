package com.malkinfo.janataaapp.models.launch

import com.google.gson.annotations.SerializedName

class FeatureEnableData(
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("user")
    var featureEnable: ArrayList<String>? = null,
    @SerializedName("_id")
    var userid: String? = null
)
