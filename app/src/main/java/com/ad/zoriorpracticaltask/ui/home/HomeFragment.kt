package com.ad.zoriorpracticaltask.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ad.zoriorpracticaltask.R
import com.ad.zoriorpracticaltask.data.AppPreferences
import com.ad.zoriorpracticaltask.data.network.ApiService
import com.ad.zoriorpracticaltask.data.network.ResultState
import com.ad.zoriorpracticaltask.data.repository.UserRepository
import com.ad.zoriorpracticaltask.data.response.User
import com.ad.zoriorpracticaltask.databinding.FragmentHomeBinding
import com.ad.zoriorpracticaltask.ui.authentication.MainActivity
import com.ad.zoriorpracticaltask.ui.shared.BaseFragment
import com.ad.zoriorpracticaltask.util.*
import com.bumptech.glide.Glide

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding, UserRepository>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.progressBar.visible(false)

        viewModel.getUserById(AppPreferences.userId.toString())

        viewModel.userResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ResultState.Success -> {
                    binding.progressBar.visible(false)
                    if (it.value.status == 1) {
                        it.value.result?.user?.let {
                            updateUi(it)
                        }
                    } else {
                        binding.root.snackBar(it.value.message.toString())
                    }
                }
                is ResultState.Failure -> {
                    handleApiError(it)
                }
                is ResultState.Loading -> {
                    binding.progressBar.visible(true)
                }
            }
        }
        binding.btnLogOut.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        AppPreferences.clearPref()
        requireActivity().startNewActivity(MainActivity::class.java)
    }

    private fun updateUi(user: User) {
        with(binding) {
            user.let { user ->
                edtFirstName.setText(user.firstname.toString())
                edtLastName.setText("")
                edtEmail.setText(user.email.toString())
                radioGroupGender.check(setChecked(user.gender.toString()))
                edtAge.setText(user.age.toString())
                Glide.with(imgProfile.context).load(user.photo)
                    .error(R.drawable.ic_launcher_background).into(imgProfile)
            }
        }
    }

    override fun getViewModel() = HomeViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(layoutInflater)

    override fun getFragmentRepository() =
        UserRepository(retrofitService.buildApi(ApiService::class.java))
}