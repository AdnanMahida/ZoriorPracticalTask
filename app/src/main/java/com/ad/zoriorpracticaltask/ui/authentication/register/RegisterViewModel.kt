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
        firstName: String?,
        lastName: String?,
        email: String?,
        password: String?,
        gender: String?,
        age: String?,
        img: File?
    ) = viewModelScope.launch {
        val map: HashMap<String, RequestBody> = HashMap()

        firstName?.let {
            map.put("firstname", RequestBody.create(MultipartBody.FORM, it))
        }
        lastName?.let {
            map.put("lastname", RequestBody.create(MultipartBody.FORM, it))
        }
        email?.let {
            map.put("email", RequestBody.create(MultipartBody.FORM, it))
        }
        password?.let {
            map.put("password", RequestBody.create(MultipartBody.FORM, it))
        }
        gender?.let {
            map.put("gender", RequestBody.create(MultipartBody.FORM, it))
        }
        age?.let {
            map.put("age", RequestBody.create(MultipartBody.FORM, it))
        }

        map.put("user_type", RequestBody.create(MultipartBody.FORM, "2"))
        map.put("devicetoken", RequestBody.create(MultipartBody.FORM, "fsadgfergsdg"))
        map.put("devicetype", RequestBody.create(MultipartBody.FORM, "Android"))
        map.put("action", RequestBody.create(MultipartBody.FORM, "userRegistration"))
        map.put("data", RequestBody.create(MultipartBody.FORM, "form-multi-part"))

        var filePart: MultipartBody.Part? = null
        img?.let {
            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), img)
            filePart = MultipartBody.Part.createFormData(
                "photo",
                img.name,
                requestBody
            )
        }

        _registerResponse.value = ResultState.Loading
        _registerResponse.value = repository.registration(map, filePart)
    }

}