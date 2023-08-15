package com.malkinfo.janataaapp.models.community

import com.google.gson.annotations.SerializedName

class UpcomingMeetingModel(
    @SerializedName("group_id")
    var group_id: ArrayList<String>? = null,
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("event_title")
    var event_title: String? = null,
    @SerializedName("meeting_link")
    var meeting_link: String? = null,
    @SerializedName("meeting_on")
    var meeting_on: Int? = null,
    @SerializedName("meeting_ends")
    var meeting_ends: Int? = null,
    @SerializedName("posted_on")
    var posted_on: Int? = null,
    @SerializedName("__v")
    var __v: Int? = null

)