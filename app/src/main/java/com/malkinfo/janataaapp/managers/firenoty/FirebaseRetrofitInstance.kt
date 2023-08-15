package com.malkinfo.janataaapp.managers.firenoty


import com.malkinfo.janataaapp.managers.firenoty.FirebaseConstantsNoti.Companion.FIREBASE_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FirebaseRetrofitInstance {

    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(FIREBASE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api by lazy {
            retrofit.create(FirebaseNotificationAPI::class.java)
        }
    }
}