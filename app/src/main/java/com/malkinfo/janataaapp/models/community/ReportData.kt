package com.malkinfo.janataaapp.models.community

import com.google.gson.annotations.SerializedName

class ReportData (
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("report")
    var report: ReportModel? = null
)