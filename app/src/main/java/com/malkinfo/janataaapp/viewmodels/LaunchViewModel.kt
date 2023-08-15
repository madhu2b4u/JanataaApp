package com.malkinfo.janataaapp.viewmodels

import MyCommunityApp
import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.enums.LoaderStatus
import com.malkinfo.janataaapp.managers.utils.SharedPrefManager
import com.malkinfo.janataaapp.managers.firenoty.FirebaseRetrofitInstance
import com.malkinfo.janataaapp.managers.utils.RetrofitManager
import com.malkinfo.janataaapp.models.firenotification.FirebasePushNotification
import com.malkinfo.janataaapp.models.launch.*
import com.malkinfo.janataaapp.models.response.Base.BaseData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LaunchViewModel(application: Application) : MyBaseViewModel(application) {
    var loginLiveData: MutableLiveData<LoginData> = MutableLiveData<LoginData>()
    var resendOtpLiveData: MutableLiveData<LoginData> = MutableLiveData<LoginData>()
    var renewTokenLiveData: MutableLiveData<RenewAccessData> = MutableLiveData<RenewAccessData>()
    var validateOtpLiveData: MutableLiveData<OtpData> = MutableLiveData<OtpData>()
    var activateLiveData: MutableLiveData<BaseData> = MutableLiveData<BaseData>()


    /**get User List*/

    fun doLogin(gsonObject: JsonObject) {
        isLoading.postValue(LoaderStatus.loading)


        CoroutineScope(exceptionHandler).launch {

            val request = RetrofitManager.getInstance(getApplication())
                .getLaunchApi().doLogin(gsonObject)


            val response = request.await()
            val apiResponse = response.body()


            if (isResponseSuccess(response)) {

                if (apiResponse!!.loginData?.success!!) {

                    SharedPrefManager.getInstance(getApplication()).setPreference(
                        MyCommunityAppConstants.AUTH_TOKEN,
                        apiResponse.loginData?.token)

                    SharedPrefManager.getInstance(getApplication()).setPreference(
                        MyCommunityAppConstants.REFRESH_TOKEN,
                        apiResponse.loginData?.refreshToken)

                    SharedPrefManager.getInstance(getApplication()).setPreference(
                        MyCommunityAppConstants.IS_SIGNUP,
                        apiResponse.loginData?.existingUser!!)
                    if (apiResponse.loginData?.existingUser!!) {


                        MyCommunityApp.setUser(getApplication(),apiResponse.loginData?.user!!)

                        if (apiResponse.loginData?.user!!.matrimony_registeration != null) {

                            MyCommunityApp.setMatrimonyUser(
                                getApplication(),
                                apiResponse.loginData?.user!!.matrimony_registeration!!
                            )
                            SharedPrefManager.getInstance(getApplication()).setPreference(
                                MyCommunityAppConstants.IS_MATRIMONY_REGISTERED_USER,
                                true
                            )
                            if (apiResponse.loginData?.partner_preference != null) {
                                MyCommunityApp.setPartnerPreference(
                                    getApplication(),
                                    apiResponse.loginData?.partner_preference!!
                                )


                                SharedPrefManager.getInstance(getApplication()).setPreference(
                                    MyCommunityAppConstants.IS_MATRIMONY_PARTNER_PREFERENCE_FINISHED,
                                    true
                                )
                            }else{
                                loginLiveData.postValue(apiResponse.loginData)

                            }
                        }else{
                            loginLiveData.postValue(apiResponse.loginData)

                        }
                    }else{

                    }

                    loginLiveData.postValue(apiResponse.loginData)

                } else {

                    errorLiveData.postValue(apiResponse.loginData!!.message)


                }
            } else {
                errorLiveData.postValue(apiResponse!!.loginData!!.message)

            }
            isLoading.postValue(LoaderStatus.success)

        }
    }
    fun doActivate(id : String, gsonObject : JsonObject) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getBaseApi().doDeactivate(id,gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.baseData?.success!!) {
                    activateLiveData.postValue(apiResponse.baseData!!)

                }else {
                    errorLiveData.postValue(apiResponse.baseData!!.message)

                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }
    fun sendFireNotification(notification: FirebasePushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = FirebaseRetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
               // Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                //Log.e(TAG, response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    fun doGetRefreshToken(gsonObject:JsonObject) {

         isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getLaunchApi()
                .doRenewAccess(gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!
            if (isResponseSuccess(response)) {
                if (apiResponse.renewAccessData?.success!!) {
                    renewTokenLiveData.postValue(apiResponse.renewAccessData)

                } else {
                    errorLiveData.postValue("renew failed")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
             isLoading.postValue(LoaderStatus.success)
        }
    }

    fun doValidateOtp(gsonObject: JsonObject) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getLaunchApi()
                .doValidateOtp(gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.otpData?.success!!) {
                    SharedPrefManager.getInstance(getApplication()).setPreference(MyCommunityAppConstants.IS_LOGGEDIN, true)
                    validateOtpLiveData.postValue(apiResponse.otpData)
                } else {
                    errorLiveData.postValue(apiResponse.otpData!!.error)
                }
            } else {
                errorLiveData.postValue("some error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }


    fun doResendOtp(gsonObject: JsonObject) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getLaunchApi().doLogin(gsonObject)
            val response = request.await()
            val apiResponse = response.body()

            if (isResponseSuccess(response)) {
                if (apiResponse!!.loginData?.success!!) {
                    resendOtpLiveData.postValue(apiResponse.loginData)

                } else {
                    errorLiveData.postValue(apiResponse.loginData!!.message)
                }
            } else {
                errorLiveData.postValue(apiResponse!!.loginData!!.message)

            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

}