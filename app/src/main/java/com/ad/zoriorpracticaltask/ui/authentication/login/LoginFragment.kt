package com.ad.zoriorpracticaltask.ui.authentication.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation
import com.ad.zoriorpracticaltask.R
import com.ad.zoriorpracticaltask.data.AppPreferences
import com.ad.zoriorpracticaltask.data.network.ApiService
import com.ad.zoriorpracticaltask.data.network.ResultState
import com.ad.zoriorpracticaltask.data.repository.AuthRepository
import com.ad.zoriorpracticaltask.databinding.FragmentLoginBinding
import com.ad.zoriorpracticaltask.ui.home.HomeActivity
import com.ad.zoriorpracticaltask.ui.shared.BaseFragment
import com.ad.zoriorpracticaltask.util.*


class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding, AuthRepository>(),
    View.OnClickListener {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.apply {

            viewModel.loginResponse.observe(viewLifecycleOwner) {
                progressLoading.visible(it is ResultState.Loading)
                btnLogin.visible(it !is ResultState.Loading)
                when (it) {
                    is ResultState.Success -> {
                        if (it.value.status == 1) {
                            it.value.result?.user?.let {
                                AppPreferences.saveUser(it)
                                requireActivity().startNewActivity(HomeActivity::class.java)
                            }
                        } else {
                            binding.root.snackBar(it.value.message.toString())
                        }
                    }
                    is ResultState.Failure -> handleApiError(it) { login() }
                    is ResultState.Loading -> {
                        progressLoading.visible(true)
                        btnLogin.visible(false)
                    }
                }
            }

            btnRegister.setOnClickListener(this@LoginFragment)

            edtEmail.addTextChangedListener {
                val password = edtPassword.text.toString().trim()
                btnLogin.enable(it.toString().isNotEmpty() && password.isNotEmpty())
            }
            edtPassword.addTextChangedListener {
                val email = edtEmail.text.toString().trim()
                btnLogin.enable(email.isNotEmpty() && it.toString().isNotEmpty())
            }

            btnLogin.setOnClickListener {
                login()
            }
        }
    }

    private fun login() {
        if (!isValidate()) {
            binding.root.snackBar("Please fill a valid data")
            return
        }

        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        viewModel.login(email, password)
    }

    private fun isValidate(): Boolean =
        validateInput(
            binding.edtEmail,
            binding.layoutEmail
        ) && validateInput(
            binding.edtPassword,
            binding.layoutPassword
        ) && emailValidation(binding.edtEmail.text.toString())

    override fun onClick(p0: View?) {
        p0?.let {
            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun getViewModel() = LoginViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        AuthRepository(retrofitService.buildApi(ApiService::class.java))


}