package com.malkinfo.janataaapp.models.base

import com.google.gson.annotations.SerializedName

class CasteData(
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("community")
    var community: ArrayList<CasteItem>? = null
)
