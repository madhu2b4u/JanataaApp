package com.malkinfo.janataaapp.models.allpost

data
class StatusSeenData(
    val post_id:String? = null,
    val user_id:String? = null,
    val userName:String? = null,
    val userProfileImg :String? = null,
    val seentime:Long? = null
) {
}