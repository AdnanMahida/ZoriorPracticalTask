package com.ad.zoriorpracticaltask.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ad.zoriorpracticaltask.data.network.ResultState
import com.ad.zoriorpracticaltask.data.repository.UserRepository
import com.ad.zoriorpracticaltask.data.response.UserResponse
import com.ad.zoriorpracticaltask.ui.shared.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: UserRepository
) : BaseViewModel(repository) {

    private val _userResponse: MutableLiveData<ResultState<UserResponse>> = MutableLiveData()
    val userResponse: LiveData<ResultState<UserResponse>>
        get() = _userResponse

    fun getUserById(userId: String) = viewModelScope.launch {

        val params: MutableMap<String, String> = HashMap()
        params["data"] = "form-multi-part"
        params["action"] = "getUserDetails"
        params["user_id"] = userId

        _userResponse.value = ResultState.Loading
        _userResponse.value = repository.getUser(params)
    }

}