package com.malkinfo.janataaapp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.malkinfo.janataaapp.helpers.MyCommunityAppLog
import java.util.*

class MyCommunityAppLifeCycleTracker : Application.ActivityLifecycleCallbacks {

    private val TAG = "OceanAppLifeCycleTracker"
    private var numStarted = 0

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {
        if (numStarted == 0) {
            // app went to foreground
            MyCommunityAppLog.i(TAG, activity.componentName.className + " came to foreground")

            val currentLanguage = Locale.getDefault().getLanguage()

        }
        numStarted++
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
        numStarted--
        if (numStarted == 0) {
            // app went to background
            MyCommunityAppLog.i(TAG, activity.componentName.className + " went to backGround")
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityResumed(activity: Activity) {
    }
}