package com.malkinfo.janataaapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.malkinfo.janataaapp.constants.MyCommunityAppConstants
import com.malkinfo.janataaapp.enums.LoaderStatus
import com.malkinfo.janataaapp.managers.utils.SharedPrefManager
import com.malkinfo.janataaapp.managers.utils.RetrofitManager
import com.malkinfo.janataaapp.models.Matrimony.*
import com.malkinfo.janataaapp.models.base.FileUploadData
import com.malkinfo.janataaapp.models.base.StateData
import com.malkinfo.janataaapp.models.response.Base.BaseData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class MatrimonyViewModel(application: Application) : MyBaseViewModel(application) {

    var matrimonyRegistrationDetailsLiveData: MutableLiveData<MatrimonyRegistrationDetailsData> = MutableLiveData<MatrimonyRegistrationDetailsData>()
    var matrimonyProfileDetailsLiveData: MutableLiveData<MatrimonyRegistrationDetailsData> = MutableLiveData<MatrimonyRegistrationDetailsData>()
    var partnerPreferenceDetailsLiveData: MutableLiveData<MatrimonyRegistrationDetailsData> = MutableLiveData<MatrimonyRegistrationDetailsData>()
    var cityLiveData: MutableLiveData<CityData> = MutableLiveData<CityData>()
    var profileCityLiveData: MutableLiveData<CityData> = MutableLiveData<CityData>()
    var stateLiveData: MutableLiveData<StateData> = MutableLiveData<StateData>()
    var partnerStateLiveData: MutableLiveData<StateData> = MutableLiveData<StateData>()
    var profileStateLiveData: MutableLiveData<StateData> = MutableLiveData<StateData>()
    var matrimonyRegistrationLiveData: MutableLiveData<MatrimonyUserData> = MutableLiveData<MatrimonyUserData>()
    var matrimonyProfileUpdateLiveData: MutableLiveData<MatrimonyUserData> = MutableLiveData<MatrimonyUserData>()
    var partnerPreferenceLiveData: MutableLiveData<PartnerPreferenceData> = MutableLiveData<PartnerPreferenceData>()
    var updatePartnerPreferenceLiveData: MutableLiveData<PartnerPreferenceData> = MutableLiveData<PartnerPreferenceData>()
    var matchProfileLiveData: MutableLiveData<MatchProfileData> = MutableLiveData<MatchProfileData>()
    var matchProfileFilterLiveData: MutableLiveData<MatchProfileData> = MutableLiveData<MatchProfileData>()
    var matchProfileDetailsLiveData: MutableLiveData<MatchProfileDetailsData> = MutableLiveData<MatchProfileDetailsData>()
    var shortListLiveData: MutableLiveData<BaseData> = MutableLiveData<BaseData>()
    var removeShortListLiveData: MutableLiveData<BaseData> = MutableLiveData<BaseData>()
    var profileImageLiveData: MutableLiveData<FileUploadData> = MutableLiveData<FileUploadData>()
    var updateProfileImageLiveData: MutableLiveData<FileUploadData> = MutableLiveData<FileUploadData>()
    var blockMatchProfileLiveData: MutableLiveData<BaseData> = MutableLiveData<BaseData>()
    var blockMatrimonyProfileLiveData: MutableLiveData<BaseData> = MutableLiveData<BaseData>()
    var unBlockProfileLiveData: MutableLiveData<BaseData> = MutableLiveData<BaseData>()
    var shortlistProfileLiveData: MutableLiveData<ShortlistData> = MutableLiveData<ShortlistData>()
    var blockedProfileLiveData: MutableLiveData<BlockedProfileData> = MutableLiveData<BlockedProfileData>()
    var reportProfileLiveData: MutableLiveData<BaseData> = MutableLiveData<BaseData>()
    var sendMessageLiveData: MutableLiveData<MessageData> = MutableLiveData<MessageData>()
    var messageListLiveData: MutableLiveData<MessageListData> = MutableLiveData<MessageListData>()
    var chatListLiveData: MutableLiveData<ChatListData> = MutableLiveData<ChatListData>()
    var uploadChatImageLiveData: MutableLiveData<FileUploadData> = MutableLiveData<FileUploadData>()
    var clearChatImageLiveData: MutableLiveData<BaseData> = MutableLiveData<BaseData>()


    fun getMatrimonyRegisterDetails() {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication())
                .getMatrimonyApi().getMatrimonyRegisterDetails()
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.matrimonyRegistrationDetailsData?.success!!) {

                    matrimonyRegistrationDetailsLiveData.postValue(apiResponse.matrimonyRegistrationDetailsData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun getMatrimonyProfileDetails() {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication())
                .getMatrimonyApi().getMatrimonyRegisterDetails()
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.matrimonyRegistrationDetailsData?.success!!) {

                    matrimonyProfileDetailsLiveData.postValue(apiResponse.matrimonyRegistrationDetailsData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun getPartnerPreferenceDetails() {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication())
                .getMatrimonyApi().getMatrimonyRegisterDetails()
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.matrimonyRegistrationDetailsData?.success!!) {

                    partnerPreferenceDetailsLiveData.postValue(apiResponse.matrimonyRegistrationDetailsData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun getCity(stateId: String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication())
                .getMatrimonyApi().getCity(stateId)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.cityData?.success!!) {

                    cityLiveData.postValue(apiResponse.cityData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun getProfileCity(stateId: String) {
        isLoading.postValue(LoaderStatus.loading)
        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication())
                .getMatrimonyApi().getCity(stateId)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.cityData?.success!!) {

                    profileCityLiveData.postValue(apiResponse.cityData)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some error")
            }
            isLoading.postValue(LoaderStatus.success)
        }
    }

    fun getProfileStateList(countryId : String) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getMatrimonyApi().getState(countryId)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.stateData?.success!!) {
                    profileStateLiveData.postValue(apiResponse.stateData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun getStateList(countryId: String) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getMatrimonyApi().getState(countryId)
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

    fun getPartnerStateList(countryId: String) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getMatrimonyApi().getState(countryId)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.stateData?.success!!) {
                    partnerStateLiveData.postValue(apiResponse.stateData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doMatrimonyRegistration(gsonObject: JsonObject) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getMatrimonyApi()
                .doMatrimonyRegistration(gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.matrimonyUserDetailsData?.success!!) {
                    SharedPrefManager.getInstance(getApplication()).setPreference(
                        MyCommunityAppConstants.IS_MATRIMONY_REGISTERED_USER, true
                    )
                    MyCommunityApp.setMatrimonyUser(getApplication(), apiResponse.matrimonyUserDetailsData?.matrimony!!)

                    matrimonyRegistrationLiveData.postValue(apiResponse.matrimonyUserDetailsData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doMatrimonyProfileUpdate(gsonObject: JsonObject) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getMatrimonyApi()
                .doMatrimonyRegistration(gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.matrimonyUserDetailsData?.success!!) {
                    MyCommunityApp.setMatrimonyUser(getApplication(), apiResponse.matrimonyUserDetailsData?.matrimony!!)
                    matrimonyProfileUpdateLiveData.postValue(apiResponse.matrimonyUserDetailsData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doPartnerPreference(gsonObject: JsonObject) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getMatrimonyApi()
                .doPartnerPreference(gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.partnerPreferenceData?.success!!) {
                    SharedPrefManager.getInstance(getApplication()).setPreference(MyCommunityAppConstants.IS_MATRIMONY_PARTNER_PREFERENCE_FINISHED, true)

                    MyCommunityApp.setPartnerPreference(getApplication(),
                        apiResponse.partnerPreferenceData?.partnerPreference!!
                    )
                    partnerPreferenceLiveData.postValue(apiResponse.partnerPreferenceData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }


    fun doUpdatePartnerPreference(id: String,gsonObject: JsonObject) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getMatrimonyApi().doUpdatePartnerPreference(id,gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.partnerPreferenceData?.success!!) {
                    SharedPrefManager.getInstance(getApplication()).setPreference(MyCommunityAppConstants.IS_MATRIMONY_PARTNER_PREFERENCE_FINISHED, true)

                    MyCommunityApp.setPartnerPreference(getApplication(),
                        apiResponse.partnerPreferenceData?.partnerPreference!!
                    )
                    updatePartnerPreferenceLiveData.postValue(apiResponse.partnerPreferenceData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doDiscoverMatchProfile(page:String,limit: String) {
        //isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getMatrimonyApi().getMatchProfile(page,limit)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.matchProfileData?.success!!) {

                    matchProfileLiveData.postValue(apiResponse.matchProfileData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doDiscoverMatchFilterProfile(parterPreferenceId: String,filterType: String,page: String,limit: String) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getMatrimonyApi()
                    .getMatchProfileFilter(parterPreferenceId,filterType,page,limit)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.matchProfileData?.success!!) {

                    matchProfileFilterLiveData.postValue(apiResponse.matchProfileData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doShortlist(id: String) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getMatrimonyApi().doShortlist(id)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.baseData?.success!!) {

                    shortListLiveData.postValue(apiResponse.baseData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doRemoveShortlist(id: String) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getMatrimonyApi().doShortlist(id)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.baseData?.success!!) {

                    shortListLiveData.postValue(apiResponse.baseData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doGetMatchProfileDetails(id: String) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getMatrimonyApi()
                .getMatchProfileDetails(id)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.matchProfileDetailsData?.success!!) {

                    matchProfileDetailsLiveData.postValue(apiResponse.matchProfileDetailsData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun uploadMultipleProfileImage(body: Array<MultipartBody.Part?>) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getBaseApi().multipleFileUpload(body)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.fileUploadData?.success!!) {

                    profileImageLiveData.postValue(apiResponse.fileUploadData)
                } else {
                    errorLiveData.postValue(apiResponse.fileUploadData!!.message)
                }
            } else {
                errorLiveData.postValue(apiResponse.fileUploadData!!.message)
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun uploadProfileImage(body: MultipartBody.Part) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getBaseApi().fileUpload(body)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.fileUploadData?.success!!) {

                    profileImageLiveData.postValue(apiResponse.fileUploadData)
                } else {
                    errorLiveData.postValue(apiResponse.fileUploadData!!.message)
                }
            } else {
                errorLiveData.postValue(apiResponse.fileUploadData!!.message)
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun updateMultipleProfileImage(body: Array<MultipartBody.Part?>) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getBaseApi().multipleFileUpload(body)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.fileUploadData?.success!!) {

                    updateProfileImageLiveData.postValue(apiResponse.fileUploadData)
                } else {
                    errorLiveData.postValue(apiResponse.fileUploadData!!.message)
                }
            } else {
                errorLiveData.postValue(apiResponse.fileUploadData!!.message)
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun updateProfileImage(body: MultipartBody.Part) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getBaseApi().fileUpload(body)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.fileUploadData?.success!!) {

                    updateProfileImageLiveData.postValue(apiResponse.fileUploadData)
                } else {
                    errorLiveData.postValue(apiResponse.fileUploadData!!.message)
                }
            } else {
                errorLiveData.postValue(apiResponse.fileUploadData!!.message)
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doBlockMatchProfile(id: String) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getMatrimonyApi()
                .doMatchProfileBlock(id)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.baseData?.success!!) {

                    blockMatchProfileLiveData.postValue(apiResponse.baseData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doBlockMatrimonyProfile(id: String) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getMatrimonyApi()
                .doMatchProfileBlock(id)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.baseData?.success!!) {

                    blockMatrimonyProfileLiveData.postValue(apiResponse.baseData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doUnBlockProfile(id: String) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getMatrimonyApi()
                .doMatchProfileBlock(id)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.baseData?.success!!) {

                    unBlockProfileLiveData.postValue(apiResponse.baseData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun getShortlistProfile() {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getMatrimonyApi().getShortlist()
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.shortlistProfileData?.success!!) {

                    shortlistProfileLiveData.postValue(apiResponse.shortlistProfileData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun getBlockedProfile(id : String) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getMatrimonyApi().getBlock(id)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.blockedProfileData?.success!!) {

                    blockedProfileLiveData.postValue(apiResponse.blockedProfileData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doReportMatrimonyProfile(gsonObject: JsonObject) {

        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getMatrimonyApi().doReportMatrimonyProfile(gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.baseData?.success!!) {

                    reportProfileLiveData.postValue(apiResponse.baseData!!)

                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doSendMessage(gsonObject: JsonObject){
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getMatrimonyApi().doSendMessage(gsonObject)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.messageData?.success!!) {
                    sendMessageLiveData.postValue(apiResponse.messageData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun getMessageList(receiver_id:String ,page: String,limit: String){
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getMatrimonyApi().getMessageList(receiver_id,page,limit)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.messageListData?.success!!) {
                    messageListLiveData.postValue(apiResponse.messageListData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun getChatList(){
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request =
                RetrofitManager.getInstance(getApplication()).getMatrimonyApi().getChatList()
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.chatListData?.success!!) {
                    chatListLiveData.postValue(apiResponse.chatListData!!)
                } else {
                    errorLiveData.postValue("Some error")
                }
            } else {
                errorLiveData.postValue("Some Error")
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun uploadChatMultipleImage(body: Array<MultipartBody.Part?>) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getBaseApi().multipleFileUpload(body)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.fileUploadData?.success!!) {

                    uploadChatImageLiveData.postValue(apiResponse.fileUploadData)
                } else {
                    errorLiveData.postValue(apiResponse.fileUploadData!!.message)
                }
            } else {
                errorLiveData.postValue(apiResponse.fileUploadData!!.message)
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun uploadChatImage(body: MultipartBody.Part) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getBaseApi().fileUpload(body)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.fileUploadData?.success!!) {

                    uploadChatImageLiveData.postValue(apiResponse.fileUploadData)
                } else {
                    errorLiveData.postValue(apiResponse.fileUploadData!!.message)
                }
            } else {
                errorLiveData.postValue(apiResponse.fileUploadData!!.message)
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }

    fun doClearChat(receiver_id: String) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {
            val request = RetrofitManager.getInstance(getApplication()).getMatrimonyApi().doClearChat(receiver_id)
            val response = request.await()
            val apiResponse = response.body()!!

            if (isResponseSuccess(response)) {
                if (apiResponse.baseData?.success!!) {

                    clearChatImageLiveData.postValue(apiResponse.baseData)
                } else {
                    errorLiveData.postValue(apiResponse.baseData!!.message)
                }
            } else {
                errorLiveData.postValue(apiResponse.baseData!!.message)
            }
            isLoading.postValue(LoaderStatus.success)

        }
    }
}