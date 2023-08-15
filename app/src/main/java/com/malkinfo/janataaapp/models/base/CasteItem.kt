package com.malkinfo.janataaapp.models.base

import com.google.gson.annotations.SerializedName

class CasteItem (
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("caste")
    var caste: String? = null,
    @SerializedName("image_url")
    var image_url: String? = null,
    @SerializedName("__v")
    var __v: Int? = null,
    @SerializedName("about")
    var about: String? = null,
    @SerializedName("isChecked")
    var isChecked: Boolean? = false
)
