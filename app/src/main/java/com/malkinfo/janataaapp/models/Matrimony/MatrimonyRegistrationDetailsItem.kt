package com.malkinfo.janataaapp.models.Matrimony

import com.google.gson.annotations.SerializedName
import com.malkinfo.janataaapp.models.base.StateItem

class MatrimonyRegistrationDetailsItem(

    @SerializedName("mothertongues")
    var mothertongues: ArrayList<MotherTongueItem>? = null,
    @SerializedName("country")
    var country: ArrayList<CountryItem>? = null,
    @SerializedName("state")
    var state: ArrayList<StateItem>? = null,
    @SerializedName("employed_id")
    var employed_id: ArrayList<EmploymentItem>? = null,
    @SerializedName("star")
    var star: ArrayList<StarItem>? = null,
    @SerializedName("moon")
    var moon: ArrayList<MoonItem>? = null


)