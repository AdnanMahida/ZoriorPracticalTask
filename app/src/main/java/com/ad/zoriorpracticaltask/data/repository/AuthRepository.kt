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
        params: Map<String, RequestBody>,
        img: MultipartBody.Part?
    ) = apiCall {
        api.registration(params, img)
    }

}