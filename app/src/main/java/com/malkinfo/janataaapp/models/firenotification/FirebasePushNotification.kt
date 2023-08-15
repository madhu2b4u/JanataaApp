package com.malkinfo.janataaapp.models.firenotification

data class FirebasePushNotification(
    val data: FirebaseNotificationData,
    val to: String
) {
}