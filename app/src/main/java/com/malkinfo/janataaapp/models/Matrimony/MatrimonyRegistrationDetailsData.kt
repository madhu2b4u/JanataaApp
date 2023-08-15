package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName

class MatrimonyRegistrationDetailsData (
    @SerializedName("success")
    var success : Boolean? = null,
    @SerializedName("apis")
    var userDetails: MatrimonyRegistrationDetailsItem?= null
)