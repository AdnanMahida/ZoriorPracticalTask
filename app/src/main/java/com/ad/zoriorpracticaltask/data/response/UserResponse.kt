package com.ad.zoriorpracticaltask.data.response


import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("MESSAGE")
    var message: String? = "",
    @SerializedName("RESULT")
    var result: Result? = Result(),
    @SerializedName("STATUS")
    var status: Int? = 0
)