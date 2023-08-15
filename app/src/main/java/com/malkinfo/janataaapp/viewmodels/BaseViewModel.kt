package com.malkinfo.janataaapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.JsonObject
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.enums.LoaderStatus
import com.malkinfo.janataaapp.managers.utils.RetrofitManager
import com.malkinfo.janataaapp.models.base.*
import com.malkinfo.janataaapp.models.response.Base.BaseData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.time.LocalDate


class BaseViewModel (application: Application): MyBaseViewModel(application){
    var countryLiveData: MutableLiveData<CountryData> = MutableLiveData<CountryData>()
    var stateLiveData: MutableLiveData<StateData> = MutableLiveData<StateData>()
    var districtLiveData: MutableLiveData<DistrictData> = MutableLiveData<DistrictData>()
    var mandalLiveData: MutableLiveData<MandalData> = MutableLiveData<MandalData>()
    var signUpMandalLiveData: MutableLiveData<MandalData> = MutableLiveData<MandalData>()
    var villageLiveData: MutableLiveData<VillageData> = MutableLiveData<VillageData>()
    var casteLiveData: MutableLiveData<CasteData> = MutableLiveData<CasteData>()
    var deactivateLiveData: MutableLiveData<BaseData> = MutableLiveData<BaseData>()
    var deleteLiveDataUser: MutableLiveData<BaseData> = MutableLiveData<BaseData>()


    var fileUploadLiveData: MutableLiveData<FileUploadData> = MutableLiveData<FileUploadData>()
    var fireDataIns: FirebaseDatabase? = null
    var database: DatabaseReference? = null

    fun getCountryList(){
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getBaseApi().getCountry()
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.countryData?.success!!) {
                    countryLiveData.postValue(apiResponse.countryData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun getStateList(countryId : String) {
         isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getBaseApi().getState(countryId)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.stateData?.success!!) {
                    stateLiveData.postValue(apiResponse.stateData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
              isLoading.postValue(LoaderStatus.success)

        }
    }

    fun getDistrictList(stateId: String) {
         isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getBaseApi().getDistrict(stateId)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.districtData?.success!!) {
                    districtLiveData.postValue(apiResponse.districtData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
              isLoading.postValue(LoaderStatus.success)

        }
    }

    fun getSignupMandalList(districtId: String) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getBaseApi().getMandalList(districtId)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.mandalData?.success!!) {
                    signUpMandalLiveData.postValue(apiResponse.mandalData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun getMandalList(districtId: String) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getBaseApi().getMandalList(districtId)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.mandalData?.success!!) {
                    mandalLiveData.postValue(apiResponse.mandalData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                    errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun getVillageList(mandalId: String) {
         isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getBaseApi().
            getVillage(mandalId)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.villageData?.success!!) {
                    villageLiveData.postValue(apiResponse.villageData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
              isLoading.postValue(LoaderStatus.success)

        }
    }

    fun getCasteList() {
         isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getBaseApi().getCaste()
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.casteData?.success!!) {
                    casteLiveData.postValue(apiResponse.casteData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
              isLoading.postValue(LoaderStatus.success)

        }
    }

    fun uploadFile(body: MultipartBody.Part) {
          isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getBaseApi().fileUpload(body)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.fileUploadData?.success!!) {

                    fileUploadLiveData.postValue(apiResponse.fileUploadData)
                } else {
                    errorLiveData.postValue(apiResponse.fileUploadData!!.message)
                }
            } else {
                errorLiveData.postValue(apiResponse.fileUploadData!!.message)
            }
               isLoading.postValue(LoaderStatus.success)

        }
    }
    fun doDeleteUSerById(id: String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication())
                    .getBaseApi().doDeleteUserBYId(id)
            val response = request.await()

            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.baseData?.success!!) {

                    deleteLiveDataUser.postValue(apiResponse.baseData)

                } else {
                    errorLiveData.postValue("Some error")

                }
            } else {
                errorLiveData.postValue("Some error")

            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun doDeactivate(id : String, gsonObject : JsonObject) {
        isLoading.postValue(LoaderStatus.loading)
        fireDataIns = FirebaseDatabase.getInstance()
        database = fireDataIns!!.getReference(MyCommunityAppConstants.FIRE_USER_DATA)
        val user = FirebaseAuth.getInstance().getCurrentUser()
        val userUid = user!!.uid

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication())
                .getBaseApi().doDeactivate(id,gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.baseData?.success!!) {
                    deactivateLiveData.postValue(apiResponse.baseData!!)
                    val token = RetrofitManager.getInstance(getApplication()).getToken()
                    database!!.child(userUid).child("token").setValue(token)
                    RetrofitManager.getInstance(getApplication()).getToken()

                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }




}