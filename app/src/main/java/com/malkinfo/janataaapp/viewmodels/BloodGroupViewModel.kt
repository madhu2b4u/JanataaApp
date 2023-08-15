package com.malkinfo.janataaapp.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.malkinfo.janataaapp.enums.LoaderStatus
import com.malkinfo.janataaapp.managers.utils.RetrofitManager
import com.malkinfo.janataaapp.models.bloodgroup.BloodGroupRequestData
import com.malkinfo.janataaapp.models.bloodgroup.BloodRequestData
import com.malkinfo.janataaapp.models.response.Base.BaseData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class BloodGroupViewModel (application: Application): MyBaseViewModel(application){

    var bloodGroupRequestListLiveData: MutableLiveData<BloodGroupRequestData> = MutableLiveData<BloodGroupRequestData>()
    var bloodGroupRequestFilterListLiveData: MutableLiveData<BloodGroupRequestData> = MutableLiveData<BloodGroupRequestData>()
    var bloodGroupRequestLiveData: MutableLiveData<BloodRequestData> = MutableLiveData<BloodRequestData>()
    var deleteMyBloodGroupRequestLiveData: MutableLiveData<BaseData> = MutableLiveData<BaseData>()

    fun getBloodGroupRequestList(page: String ,limit : String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getBloodGroupApi().doGetBloodGroupRequest(page ,limit)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.bloodGroupRequestData?.success!!) {

                    bloodGroupRequestListLiveData.postValue(apiResponse.bloodGroupRequestData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }


    fun doBloodGroupRequest(gsonObject : JsonObject) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getBloodGroupApi().doBloodRequest(gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.bloodRequestData?.success!!) {

                    bloodGroupRequestLiveData.postValue(apiResponse.bloodRequestData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun doFindBloodGroupRequest(gsonObject: JsonObject) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).
            getBloodGroupApi().doFindBloodGroupRequest(gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.bloodGroupRequestData?.success!!) {

                    bloodGroupRequestFilterListLiveData.postValue(apiResponse.bloodGroupRequestData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun doDeleteMyBloodGroupRequest(id : String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).
            getBloodGroupApi().doDeleteMyBloodGroupRequest(id)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.baseData?.success!!) {

                    deleteMyBloodGroupRequestLiveData.postValue(apiResponse.baseData)
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