package com.malkinfo.janataaapp.managers.firenoty

import com.malkinfo.janataaapp.managers.firenoty.FirebaseConstantsNoti.Companion.FIRE_CONTENT_TYPE
import com.malkinfo.janataaapp.managers.firenoty.FirebaseConstantsNoti.Companion.Firebase_SERVER_KEY
import com.malkinfo.janataaapp.models.firenotification.FirebasePushNotification
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import okhttp3.ResponseBody
import retrofit2.Response



interface FirebaseNotificationAPI {

    @Headers("Authorization: key=$Firebase_SERVER_KEY", "Content-Type:$FIRE_CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: FirebasePushNotification
    ): Response<ResponseBody>
}