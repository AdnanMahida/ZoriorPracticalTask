package com.ad.zoriorpracticaltask.data.repository

import com.ad.zoriorpracticaltask.data.network.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AuthRepository(
    private val api: ApiService,
) : BaseRepository() {

    suspend fun login(params: Map<String, String>) = apiCall {
        api.login(params)
    }

    suspend fun registration(
        firstName: RequestBody,
        lastname: RequestBody,
        email: RequestBody,
        password: RequestBody,
        gender: RequestBody,
        age: RequestBody,
        userType: RequestBody,
        deviceToken: RequestBody,
        deviceType: RequestBody,
        action: RequestBody,
        data: RequestBody,
        photo: MultipartBody.Part
    ) = apiCall {
        api.registration(
            firstName,
            lastname,
            email,
            password,
            gender,
            age,
            userType,
            deviceToken,
            deviceType,
            action,
            data,
            photo
        )
    }

}