package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName
import com.malkinfo.janataaapp.models.base.StateItem

class PartnerDetailsItem (
    @SerializedName("profile_url")
    var profile_url: ArrayList<String>? = null,
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("star")
    var star: StarItem? = null,
    @SerializedName("mother_tongue")
    var mother_tongue: MotherTongueItem? = null,
    @SerializedName("marital_status")
    var marital_status: String? = null,
    @SerializedName("country_living_in")
    var country_living_in: CountryItem? = null,
    @SerializedName("state_living_in")
    var state_living_in: StateItem? = null,
    @SerializedName("city_living_in")
    var city_living_in: CityItem? = null,
    @SerializedName("height")
    var height: Int? = null,
    @SerializedName("moon")
    var moon: MoonItem? = null,
    @SerializedName("physical_status")
    var physical_status: String? = null,
    @SerializedName("body_type")
    var body_type: String? = null,
    @SerializedName("age")
    var age: Int? = null,
    @SerializedName("weight")
    var weight: Int? = null,
    @SerializedName("education")
    var education: String? = null,
    @SerializedName("employed_in")
    var employed_in: EmploymentItem? = null,
    @SerializedName("annual_income")
    var annual_income: Int? = null,
    @SerializedName("family_status")
    var family_status: String? = null,
    @SerializedName("family_type")
    var family_type: String? = null,
    @SerializedName("ancestral_origin")
    var ancestral_origin: String? = null,
    @SerializedName("assets")
    var assets: String? = null,
    @SerializedName("fathers_occupation")
    var fathers_occupation: String? = null,
    @SerializedName("mother_occupation")
    var mother_occupation: String? = null,
    @SerializedName("parent_number")
    var parent_number: String? = null,
    @SerializedName("about_myself")
    var about_myself: String? = null

)