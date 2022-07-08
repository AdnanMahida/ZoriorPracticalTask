package com.ad.zoriorpracticaltask.data.response


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("user")
    var user: User? = User()
)