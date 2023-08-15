package com.malkinfo.janataaapp.models.community

import com.google.gson.annotations.SerializedName

class MeetingData (
    @SerializedName("success")
    var success : Boolean ? =null,
    @SerializedName("upcoming_meetings")
    var upcoming_meetings: ArrayList<UpcomingMeetingModel>?= null,
    @SerializedName("recent_meetings")
    var  recent_meetings : ArrayList<RecentMeetingModel>? = null
)