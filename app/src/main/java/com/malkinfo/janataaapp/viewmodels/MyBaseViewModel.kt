package com.malkinfo.janataaapp.viewmodels

import MyCommunityApp
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.malkinfo.janataaapp.R
import com.malkinfo.janataaapp.enums.LoaderStatus
import com.malkinfo.janataaapp.managers.utils.SharedPrefManager
import com.malkinfo.janataaapp.models.response.ErrorResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.json.JSONObject
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

open class MyBaseViewModel(application: Application) : AndroidViewModel(application),
    CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + rootJob

    protected val TAG: String = this.javaClass.simpleName

    protected var errorLiveData = MutableLiveData<String?>()

    // ...because this is what we'll want to expose
    val errorMediatorLiveData = MediatorLiveData<String?>()

    var isLoading = MutableLiveData<LoaderStatus>()

    val rootJob = Job()

    init {
        errorMediatorLiveData.addSource(errorLiveData) { result: String? ->
            result?.let {
                errorMediatorLiveData.value = result

            }
        }
    }

    val sharedPrefManager: SharedPrefManager
        get() {
            return SharedPrefManager.getInstance(getApplication())
        }

    protected val exceptionHandler: CoroutineContext =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            isLoading.postValue(LoaderStatus.failed)
            errorLiveData.postValue(throwable.message)
            throwable.printStackTrace()
        }

    //Remote config
    protected val remoteConfig: FirebaseRemoteConfig by lazy {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(
                getApplication<MyCommunityApp>().resources.getInteger(
                    R.integer.REMOTE_CONFIG_INTERVAL_SECONDS
                ).toLong()
            )
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        return@lazy remoteConfig
    }

    protected fun getGsonObject(obj: JSONObject): JsonObject {
        val jsonParser = JsonParser()
        return jsonParser.parse(obj.toString()) as JsonObject
    }

    protected fun <T : Any> isResponseSuccess(response: Response<T>): Boolean {
        if (!response.isSuccessful) {
            isLoading.postValue(LoaderStatus.failed)
            if (response.errorBody() != null) {
                val jsonString = response.errorBody()!!.string()
                if (jsonString.contains("{")) {
                    val errorModel = ErrorResponse(jsonString)
                    errorLiveData.postValue(errorModel.message)
                } else {
                    errorLiveData.postValue(response.message())
                }
            } else if (!response.message().isEmpty()){
                errorLiveData.postValue(response.message())
            } else{
                errorLiveData.postValue(getApplication<MyCommunityApp>().getString(R.string.something_wrong))
            }
        }
        return response.isSuccessful
    }

    override fun onCleared() {
        super.onCleared()
        rootJob.cancel()
    }
}