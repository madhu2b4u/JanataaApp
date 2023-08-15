package com.malkinfo.janataaapp.models.firebaseuser

data class FireUser(
    var uid: String? = null,
    var _id :String? = null,
    var name: String? = null,
    var phoneNumber: String? = null,
    var status:String? = null,
    var token :String? = null,
    var user_photo_url :String? = null
) {
}