package com.malkinfo.janataaapp.models.community

import com.google.gson.annotations.SerializedName
import com.malkinfo.janataaapp.models.base.CasteItem

class GroupModel (

    @SerializedName("caste")
    var caste : ArrayList<CasteItem>?=null,
    @SerializedName("total_members")
    var  total_members: Int ?=null,
    @SerializedName("is_joined")
    var  is_joined: Boolean?=null,
    @SerializedName("profile_url")
    var profile_url: String?=null,
    @SerializedName("_id")
    var _id : String ?=null,
    @SerializedName("group")
    var  group : String ?= null,
    @SerializedName("about")
    var about : String ?=null,
    @SerializedName ("type")
    var  type : String ?= null,
    @SerializedName("created_at")
    var created_at : String?=null,
    @SerializedName("__v")
    var __v : Int ?=null,
    @SerializedName("isMute")
    var  isMute: Boolean?=true)