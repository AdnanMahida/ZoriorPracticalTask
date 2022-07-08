package com.ad.zoriorpracticaltask.data.repository

import com.ad.zoriorpracticaltask.data.network.ApiService


class UserRepository(
    private val api: ApiService
) : BaseRepository() {

    suspend fun getUser(params: Map<String, String>) = apiCall {
        api.getUserDetails(params)
    }

}