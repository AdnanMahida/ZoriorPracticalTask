package com.ad.zoriorpracticaltask.data.network

import okhttp3.ResponseBody

sealed class ResultState<out T> {
    data class Success<out T>(val value: T) : ResultState<T>()

    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : ResultState<Nothing>()

    object Loading : ResultState<Nothing>()
}
