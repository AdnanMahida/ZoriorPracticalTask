package com.ad.zoriorpracticaltask.ui.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ad.zoriorpracticaltask.data.network.ResultState
import com.ad.zoriorpracticaltask.data.repository.AuthRepository
import com.ad.zoriorpracticaltask.data.response.UserResponse
import com.ad.zoriorpracticaltask.ui.shared.BaseViewModel
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository
) : BaseViewModel(repository) {

    private val _loginResponse: MutableLiveData<ResultState<UserResponse>> = MutableLiveData()
    val loginResponse: LiveData<ResultState<UserResponse>>
        get() = _loginResponse

    fun login(email: String, password: String) = viewModelScope.launch {

        val params: MutableMap<String, String> = HashMap()
        params["data"] = "form-multi-part"
        params["action"] = "userAuthentication"
        params["email"] = email
        params["password"] = password
        params["user_type"] = "2"
        params["devicetoken"] = "fsadgfergsdg"
        params["devicetype"] = "Android"

        _loginResponse.value = ResultState.Loading
        _loginResponse.value = repository.login(params)
    }

}