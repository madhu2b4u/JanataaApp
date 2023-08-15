package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName
import com.malkinfo.janataaapp.models.base.StateItem

class BlockedProfileItem (
    @SerializedName("is_shortlisted")
    var is_shortlisted: Boolean? = null,
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("state")
    var state: StateItem? = null,
    @SerializedName("DOB")
    var DOB: String? = null,
    @SerializedName("caste")
    var caste: MatrimonyCasteItem? = null,
    @SerializedName("full_name")
    var full_name: String? = null,
    @SerializedName("profile_url")
    var profile_url: String? = null,
    @SerializedName("matrimony_registeration")
    var matrimony_registeration: PreferenceItem? = null
)