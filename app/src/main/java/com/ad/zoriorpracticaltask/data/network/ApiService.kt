package com.ad.zoriorpracticaltask.data.network

import com.ad.zoriorpracticaltask.data.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("userAuthentication.php")
    suspend fun login(
        @FieldMap params: Map<String, String>
    ): UserResponse


    @FormUrlEncoded
    @POST("getUserDetails.php")
    suspend fun getUserDetails(
        @FieldMap params: Map<String, String>
    ): UserResponse


    @Multipart
    @POST("userRegistration.php")
    suspend fun registration(
        @Part("firstname") firstName: RequestBody,
        @Part("lastname") lastname: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("age") age: RequestBody,
        @Part("user_type") userType: RequestBody,
        @Part("devicetoken") deviceToken: RequestBody,
        @Part("devicetype") deviceType: RequestBody,
        @Part("action") action: RequestBody,
        @Part("data") data: RequestBody,
        @Part photo: MultipartBody.Part
    ): UserResponse

}