package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName
import com.malkinfo.janataaapp.models.ProfileRecommendationItem

class MatchProfileDetailsData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("user")
    var user: MatchProfileDetailsItem? = null,
    @SerializedName("recommended")
    var recommended: ArrayList<ProfileRecommendationItem>? = null
)