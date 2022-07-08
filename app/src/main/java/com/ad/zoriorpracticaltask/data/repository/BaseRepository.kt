package com.ad.zoriorpracticaltask.data.repository


import android.util.Log
import com.ad.zoriorpracticaltask.data.network.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {
    suspend fun <T> apiCall(
        apiCall: suspend () -> T
    ): ResultState<T> {
        return withContext(Dispatchers.IO) {
            try {
                ResultState.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        ResultState.Failure(
                            isNetworkError = false,
                            throwable.code(),
                            throwable.response()?.errorBody()
                        )
                    }
                    else -> {
                        ResultState.Failure(isNetworkError = true, null, null)
                    }
                }
            }
        }
    }
}