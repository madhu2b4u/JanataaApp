package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName


class PreferenceItem (
    @SerializedName("height")
    var height: Int? = null,
    @SerializedName("age")
    var age: Int? = null,
    @SerializedName("education")
    var education: String? = null,
    @SerializedName("employed_in")
    var employed_in: EmploymentItem? = null
)