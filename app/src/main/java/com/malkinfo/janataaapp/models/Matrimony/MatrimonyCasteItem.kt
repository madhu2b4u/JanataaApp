package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName

class MatrimonyCasteItem (
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("caste")
    var caste: String? = null,
    @SerializedName("image_url")
    var image_url: String? = null,
    @SerializedName("__v")
    var __v: Int? = null,
    @SerializedName("about")
    var about: String? = null
)