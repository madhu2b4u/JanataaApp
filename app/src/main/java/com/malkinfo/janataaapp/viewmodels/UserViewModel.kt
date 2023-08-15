package com.malkinfo.janataaapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.enums.LoaderStatus
import com.malkinfo.janataaapp.managers.utils.SharedPrefManager
import com.malkinfo.janataaapp.managers.utils.RetrofitManager
import com.malkinfo.janataaapp.models.launch.FeatureEnableData
import com.malkinfo.janataaapp.models.launch.IdentityCardData
import com.malkinfo.janataaapp.models.launch.SignUpData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : MyBaseViewModel(application)  {

    var signUpLiveData: MutableLiveData<SignUpData> = MutableLiveData<SignUpData>()
    var updateProfileLiveData: MutableLiveData<SignUpData> = MutableLiveData<SignUpData>()
    var identityCardLiveData: MutableLiveData<IdentityCardData> = MutableLiveData<IdentityCardData>()
    var featureEnableLiveData: MutableLiveData<FeatureEnableData> = MutableLiveData<FeatureEnableData>()
    var communityFeatureEnableLiveData: MutableLiveData<FeatureEnableData> = MutableLiveData<FeatureEnableData>()
    var bloodGroupFeatureEnableLiveData: MutableLiveData<FeatureEnableData> = MutableLiveData<FeatureEnableData>()
    var matrimonyFeatureEnableLiveData: MutableLiveData<FeatureEnableData> = MutableLiveData<FeatureEnableData>()
    var meetingFeatureEnableLiveData: MutableLiveData<FeatureEnableData> = MutableLiveData<FeatureEnableData>()


    fun doSignUp(gsonObject: JsonObject) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getUserApi().doSignUp(gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                SharedPrefManager.getInstance(getApplication()).setPreference(MyCommunityAppConstants.IS_SIGNUP, true)

                if (apiResponse.signUpData?.success!!) {
                    MyCommunityApp.setUser(getApplication(), apiResponse.signUpData?.user!!)
                    signUpLiveData.postValue(apiResponse.signUpData)
                } else {
                    errorLiveData.postValue(apiResponse.signUpData!!.message)
                }
            } else {
                errorLiveData.postValue(apiResponse.signUpData!!.message)
            }
              isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doUpdateProfile(gsonObject: JsonObject) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getUserApi().doSignUp(gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {

                if (apiResponse.signUpData?.success!!) {
                    MyCommunityApp.setUser(getApplication(), apiResponse.signUpData?.user!!)
                    updateProfileLiveData.postValue(apiResponse.signUpData)

                } else {
                    errorLiveData.postValue(apiResponse.signUpData!!.message)
                }
            } else {
                errorLiveData.postValue(apiResponse.signUpData!!.message)
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun getIdentityCard() {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getUserApi().getIdentityCard()
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {

                if (apiResponse.identityCardData?.success!!) {
                    identityCardLiveData.postValue(apiResponse.identityCardData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
              isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doFeatureEnable(gsonObject: JsonObject) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getUserApi().doFeatureEnable(gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                SharedPrefManager.getInstance(getApplication()).setPreference(MyCommunityAppConstants.IS_FEATURE_ENABLE_FINISHED,true)
                if (apiResponse.featureEnableData?.success!!) {
                    featureEnableLiveData.postValue(apiResponse.featureEnableData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
              isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doCommunityFeatureEnable(gsonObject: JsonObject) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getUserApi().doFeatureEnable(gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                SharedPrefManager.getInstance(getApplication()).setPreference(MyCommunityAppConstants.IS_COMMUNITYGROUP_ENABLE, true)
                if (apiResponse.featureEnableData?.success!!) {
                    communityFeatureEnableLiveData.postValue(apiResponse.featureEnableData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }
    fun doMEETINGFeatureEnable(gsonObject: JsonObject) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getUserApi().doFeatureEnable(gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                SharedPrefManager.getInstance(getApplication()).setPreference(MyCommunityAppConstants.IS_MEET_GROUP_ENABLE, true)
                if (apiResponse.featureEnableData?.success!!) {
                    meetingFeatureEnableLiveData.postValue(apiResponse.featureEnableData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doBloodGroupFeatureEnable(gsonObject: JsonObject) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getUserApi().doFeatureEnable(gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                SharedPrefManager.getInstance(getApplication()).setPreference(MyCommunityAppConstants.IS_BLOODGROUP_ENABLE, true)
                if (apiResponse.featureEnableData?.success!!) {
                    bloodGroupFeatureEnableLiveData.postValue(apiResponse.featureEnableData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doMatrimonyFeatureEnable(gsonObject: JsonObject) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getUserApi().doFeatureEnable(gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                SharedPrefManager.getInstance(getApplication()).setPreference(MyCommunityAppConstants.IS_MATRIMONY_ENABLE, true)
                if (apiResponse.featureEnableData?.success!!) {
                    matrimonyFeatureEnableLiveData.postValue(apiResponse.featureEnableData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

}