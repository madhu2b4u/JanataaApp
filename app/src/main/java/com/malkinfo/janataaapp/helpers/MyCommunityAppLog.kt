package com.malkinfo.janataaapp.helpers

import android.util.Log

object MyCommunityAppLog {
    fun e(TAG: String, MESSAGE: String) {
        if (MyCommunityApp.DEBUG)
            Log.e(TAG, MESSAGE)
    }

    fun d(TAG: String, MESSAGE: String) {
        if (MyCommunityApp.DEBUG)
            Log.d(TAG, MESSAGE)
    }

    fun i(TAG: String, MESSAGE: String) {
        if (MyCommunityApp.DEBUG)
            Log.i(TAG, MESSAGE)
    }

    fun v(TAG: String, MESSAGE: String) {
        if (MyCommunityApp.DEBUG)
            Log.v(TAG, MESSAGE)
    }

    fun w(TAG: String, MESSAGE: String) {
        if (MyCommunityApp.DEBUG)
            Log.w(TAG, MESSAGE)
    }
}