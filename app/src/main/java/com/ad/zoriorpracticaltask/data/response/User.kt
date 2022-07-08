package com.ad.zoriorpracticaltask.data.response


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("activation_token")
    var activationToken: String? = "",
    @SerializedName("age")
    var age: String? = "",
    @SerializedName("created_datetime")
    var createdDatetime: String? = "",
    @SerializedName("devicetoken")
    var devicetoken: String? = "",
    @SerializedName("devicetype")
    var devicetype: String? = "",
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("fb_id")
    var fbId: String? = "",
    @SerializedName("firstname")
    var firstname: String? = "",
    @SerializedName("gender")
    var gender: String? = "",
    @SerializedName("is_active")
    var isActive: String? = "",
    @SerializedName("lastlogin_datetime")
    var lastloginDatetime: String? = "",
    @SerializedName("password")
    var password: String? = "",
    @SerializedName("photo")
    var photo: String? = "",
    @SerializedName("user_id")
    var userId: String? = "",
    @SerializedName("user_type")
    var userType: String? = ""
)