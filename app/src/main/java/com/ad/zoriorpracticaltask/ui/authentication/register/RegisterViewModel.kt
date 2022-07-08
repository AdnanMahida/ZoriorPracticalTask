package com.ad.zoriorpracticaltask.ui.authentication.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ad.zoriorpracticaltask.data.network.ResultState
import com.ad.zoriorpracticaltask.data.repository.AuthRepository
import com.ad.zoriorpracticaltask.data.response.UserResponse
import com.ad.zoriorpracticaltask.ui.shared.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class RegisterViewModel(
    private val repository: AuthRepository
) : BaseViewModel(repository) {

    private val _registerResponse: MutableLiveData<ResultState<UserResponse>> = MutableLiveData()
    val registerResponse: LiveData<ResultState<UserResponse>>
        get() = _registerResponse

    fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        gender: String,
        age: String,
        img: File
    ) = viewModelScope.launch {

        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), img)
        val filePart = MultipartBody.Part.createFormData(
            "photo",
            img.name,
            requestBody
        )


        _registerResponse.value = ResultState.Loading
        _registerResponse.value = repository.registration(
            firstName = RequestBody.create(MultipartBody.FORM, firstName),
            lastname = RequestBody.create(MultipartBody.FORM, lastName),
            email = RequestBody.create(MultipartBody.FORM, email),
            password = RequestBody.create(MultipartBody.FORM, password),
            gender = RequestBody.create(MultipartBody.FORM, gender),
            age = RequestBody.create(MultipartBody.FORM, age),
            userType = RequestBody.create(MultipartBody.FORM, "2"),
            deviceToken = RequestBody.create(MultipartBody.FORM, "fsadgfergsdg"),
            deviceType = RequestBody.create(MultipartBody.FORM, "Android"),
            action = RequestBody.create(MultipartBody.FORM, "userRegistration"),
            data = RequestBody.create(MultipartBody.FORM, "form-multi-part"),
            photo = filePart,
        )
    }

}