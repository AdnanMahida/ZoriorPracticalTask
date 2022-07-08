package com.ad.zoriorpracticaltask.ui.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ad.zoriorpracticaltask.data.repository.AuthRepository
import com.ad.zoriorpracticaltask.data.repository.BaseRepository
import com.ad.zoriorpracticaltask.data.repository.UserRepository
import com.ad.zoriorpracticaltask.ui.authentication.login.LoginViewModel
import com.ad.zoriorpracticaltask.ui.authentication.register.RegisterViewModel
import com.ad.zoriorpracticaltask.ui.home.HomeViewModel

class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository = repository as UserRepository) as T
            else ->
                throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}