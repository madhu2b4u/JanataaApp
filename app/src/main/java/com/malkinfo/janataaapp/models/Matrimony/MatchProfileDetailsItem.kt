package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName
import com.malkinfo.janataaapp.models.base.DistrictItem
import com.malkinfo.janataaapp.models.base.StateItem
import com.malkinfo.janataaapp.models.base.VillageItem

class MatchProfileDetailsItem(
    @SerializedName("followers")
    var followers: Int? = null,
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("mobile")
    var mobile: String? = null,
    @SerializedName("__v")
    var __v: Int? = null,
    @SerializedName("address")
    var address: String? = null,
    @SerializedName("district")
    var district: DistrictItem? = null,
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
    var caste: MatrimonyCasteItem? = null,
    @SerializedName("employment_state")
    var employment_state: String? = null,
    @SerializedName("full_name")
    var full_name: String? = null,
    @SerializedName("gender")
    var gender: String? = null,
    @SerializedName("marital_status")
    var marital_status: String? = null,
    @SerializedName("parent_name")
    var parent_name: String? = null,
    @SerializedName("matrimony_registeration")
    var matrimony_registeration: PartnerDetailsItem? = null,
    @SerializedName("aadhar_number")
    var aadhar_number: String? = null,
    @SerializedName("identity_card")
    var identity_card: String? = null,
    @SerializedName("mention_work")
    var mention_work: String? = null,
    @SerializedName("is_shortlisted")
    var is_shortlisted: Boolean? = null
)