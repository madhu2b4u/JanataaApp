package com.malkinfo.janataaapp.models.statusmodel

data class StatusData(
    var postId:String?= null,
    var profileImage: String? = null,
    var statuses: UserStatusData? = null
) {
}