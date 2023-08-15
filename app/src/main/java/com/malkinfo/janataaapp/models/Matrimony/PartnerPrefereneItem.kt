package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName

class PartnerPrefereneItem(
    @SerializedName("age")
    var age: ArrayList<String>? = null,
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("star")
    var star: String? = null,
    @SerializedName("height")
    var height: Int? = null,
    @SerializedName("marital_status")
    var marital_status: String? = null,
    @SerializedName("physical_status")
    var physical_status: String? = null,
    @SerializedName("mother_tongue")
    var mother_tongue: String? = null,
    @SerializedName("country_livingin")
    var country_livingin: String? = null,
    @SerializedName("state_livingin")
    var state_livingin: String? = null,
    @SerializedName("education")
    var education: String? = null,
    @SerializedName("employed_in")
    var employed_in: String? = null,
    @SerializedName("annual_income")
    var annual_income: Int? = null,
    @SerializedName("user_id")
    var user_id: String? = null
)