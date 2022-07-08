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
    @JvmSuppressWildcards
    @POST("userRegistration.php")
    suspend fun registration(
        @PartMap params: Map<String, RequestBody>,
        @Part img: MultipartBody.Part?
    ): UserResponse

}