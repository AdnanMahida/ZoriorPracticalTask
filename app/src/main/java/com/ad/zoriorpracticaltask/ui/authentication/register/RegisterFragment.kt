package com.ad.zoriorpracticaltask.ui.authentication.register

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import com.ad.zoriorpracticaltask.R
import com.ad.zoriorpracticaltask.data.AppPreferences
import com.ad.zoriorpracticaltask.data.network.ApiService
import com.ad.zoriorpracticaltask.data.network.ResultState
import com.ad.zoriorpracticaltask.data.repository.AuthRepository
import com.ad.zoriorpracticaltask.databinding.FragmentRegisterBinding
import com.ad.zoriorpracticaltask.ui.home.HomeActivity
import com.ad.zoriorpracticaltask.ui.shared.BaseFragment
import com.ad.zoriorpracticaltask.util.*
import com.bumptech.glide.Glide
import com.permissionx.guolindev.PermissionX
import java.io.File
import java.util.*


class RegisterFragment : BaseFragment<RegisterViewModel, FragmentRegisterBinding, AuthRepository>(),
    View.OnClickListener {
    private var imgUri: Uri? = null
    private var age: String? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.apply {

            viewModel.registerResponse.observe(viewLifecycleOwner) {
                progressLoading.visible(it is ResultState.Loading)
                btnRegister.visible(it !is ResultState.Loading)
                when (it) {
                    is ResultState.Success -> {
                        Log.d("TAG",it.value.result.toString())
                        if (it.value.result?.user?.userId != null) {
                            it.value.result?.user?.let { user ->
                                AppPreferences.saveUser(user)
                                requireActivity().startNewActivity(HomeActivity::class.java)
                            }
                        } else {
                            root.snackBar(it.value.message.toString())
                        }
                    }
                    is ResultState.Failure -> {
                        handleApiError(it) { register() }
                    }
                    is ResultState.Loading -> {
                        progressLoading.visible(true)
                        btnRegister.visible(false)
                    }
                }
            }

            btnCancel.setOnClickListener(this@RegisterFragment)
            imgProfile.setOnClickListener { pickImg() }
            btnRegister.setOnClickListener { register() }
            edtBirthDate.setOnClickListener { openDobPicker() }
        }
    }

    private fun FragmentRegisterBinding.register() {
        if (!isValidate()) {
            root.snackBar("Please fill a valid data")
            return
        }

        val selectedGenderId: Int = radioGroupGender.checkedRadioButtonId

        val firstName = edtFirstName.text.toString()
        val lastName = edtLastName.text.toString()
        val email = edtEmail.text.toString()
        val password = edtPassword.text.toString()
        val gender = view?.findViewById<RadioButton>(selectedGenderId)?.text.toString()

        val img = imgUri?.let { requireContext().getRealPathFromURI(it) }?.let { File(it) }

        viewModel.register(
            firstName,
            lastName,
            email,
            password,
            gender,
            age,
            img
        )
    }


    private fun pickImg() {
        PermissionX.init(requireActivity())
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "Core fundamental are based on these permissions",
                    "OK",
                    "Cancel"
                )
            }.request { allGranted, grantedList, deniedList ->
                if (allGranted || grantedList.isNotEmpty())
                    createChooser()
            }
    }

    private fun createChooser() {
        val chooseIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launchSomeActivity.launch(chooseIntent)
    }

    private var launchSomeActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                imgUri = result.data?.data
                Glide.with(binding.imgProfile).load(imgUri).into(binding.imgProfile)
            }
        }

    private fun openDobPicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            requireActivity(),
            { _, year, monthOfYear, dayOfMonth ->
                val date = "${dayOfMonth}/${monthOfYear}/${year}"
                binding.edtBirthDate.setText(date)
                age = getAge(
                    year = year,
                    month = monthOfYear,
                    day = dayOfMonth
                )
            },
            year,
            month,
            day
        )
        dpd.datePicker.maxDate = Date().time

        dpd.show()
    }

    private fun isValidate(): Boolean =
        validateInput(
            binding.edtFirstName,
            binding.layoutFirstName
        ) && validateInput(
            binding.edtLastName,
            binding.layoutLastName
        ) && validateInput(
            binding.edtEmail,
            binding.layoutEmail
        ) && validateInput(
            binding.edtPassword,
            binding.layoutPassword
        ) && emailValidation(binding.edtEmail.text.toString())


    override fun onClick(p0: View?) {
        p0?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    override fun getViewModel() = RegisterViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegisterBinding.inflate(layoutInflater, container, false)

    override fun getFragmentRepository() =
        AuthRepository(retrofitService.buildApi(ApiService::class.java))

}