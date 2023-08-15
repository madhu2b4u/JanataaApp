package com.malkinfo.janataaapp.models.launch

import com.google.gson.annotations.SerializedName
import com.malkinfo.janataaapp.models.Matrimony.CountryItem
import com.malkinfo.janataaapp.models.Matrimony.MatrimonyUserItem
import com.malkinfo.janataaapp.models.base.*

class User(
    @SerializedName("is_following")
    var is_following: Boolean? = null,
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("mobile")
    var mobile: String? = null,
    @SerializedName("registration_number")
    var registration_number: String? = null,
    @SerializedName("address")
    var address: String? = null,
    @SerializedName("district")
    var district: DistrictItem? = null,
    @SerializedName("country")
    var country: CountryItem? = null,
    @SerializedName("mandal")
    var mandal: MandalItem? = null,
    @SerializedName("pincode")
    var pincode: String? = null,
    @SerializedName("state")
    var state: StateItem? = null,
    @SerializedName("village")
    var village: VillageItem? = null,
    @SerializedName("DOB")
    var DOB: String? = null,
    @SerializedName("blood_group")
    var blood_group: String? = null,
    @SerializedName("caste")
    var caste: CasteItem? = null,
    @SerializedName("employment_state")
    var employment_state: String? = null,
    @SerializedName("full_name")
    var full_name: String? = null,
    @SerializedName("gender")
    var gender: String? = null,
    @SerializedName("aadhar_card_front")
    var aadhar_card_front: String? = null,
    @SerializedName("aadhar_card_back")
    var aadhar_card_back: String? = null,
    @SerializedName("marital_status")
    var marital_status: String? = null,
    @SerializedName("parent_name")
    var parent_name: String? = null,
    @SerializedName("profile_url")
    var profile_url: String? = null,
    @SerializedName("aadhar_number")
    var aadhar_number: String? = null,
    @SerializedName("mention_work")
    var mention_work: String? = null,
    @SerializedName("others")
    var others: String? = null,
    @SerializedName("status")
    var status:String? = null,
    @SerializedName("matrimony_registeration")
    var  matrimony_registeration: MatrimonyUserItem? = null,

    )