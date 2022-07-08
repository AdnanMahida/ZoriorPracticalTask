package com.ad.zoriorpracticaltask.ui.authentication.register

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.permissionx.guolindev.PermissionX
import java.io.File
import java.util.*


class RegisterFragment : BaseFragment<RegisterViewModel, FragmentRegisterBinding, AuthRepository>(),
    View.OnClickListener {
    private var picturePath: File? = null
    private var age: String? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.apply {

            viewModel.registerResponse.observe(viewLifecycleOwner) {
                progressLoading.visible(it is ResultState.Loading)
                btnRegister.visible(it !is ResultState.Loading)
                when (it) {
                    is ResultState.Success -> {
                        it.value.result?.user?.let {
                            AppPreferences.saveUser(it)
                            requireActivity().startNewActivity(HomeActivity::class.java)
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
        if (picturePath == null) {
            root.snackBar("Please select picture")
            return
        }
        if (!isValidate()) {
            root.snackBar("Please fill the data first")
            return
        }

        val selectedId: Int = radioGroupGender.checkedRadioButtonId

        val firstName = edtFirstName.text.toString()
        val lastName = edtLastName.text.toString()
        val email = edtEmail.text.toString()
        val password = edtPassword.text.toString()
        val gender = view?.findViewById<RadioButton>(selectedId)?.text.toString()


        viewModel.register(
            firstName,
            lastName,
            email,
            password,
            gender,
            age.toString(),
            picturePath!!
        )
    }


    private fun pickImg() {
        PermissionX.init(activity)
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
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {

                    val i = Intent().also {
                        it.type = "image/*"
                        it.action = Intent.ACTION_GET_CONTENT
                    }
                    val pickIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    pickIntent.type = "image/*"

                    val chooserIntent = Intent.createChooser(i, "Select Image")
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

                    launchSomeActivity.launch(chooserIntent)

                } else {
                    Toast.makeText(
                        context,
                        "permissions required for perform this action",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private var launchSomeActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data

                if (data != null && data.data != null) {
                    requireActivity().contentResolver?.query(data.data!!, null, null, null, null)
                        ?.use {
                            if (it.moveToFirst()) {
                                picturePath = File(
                                    it.getString(it.getColumnIndex(MediaStore.MediaColumns.DATA))
                                )
                            }
                        }
                }
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
        ) && validateInput(binding.edtPassword, binding.layoutPassword)

    private fun validateInput(
        edtFirstName: TextInputEditText,
        layoutFirstName: TextInputLayout
    ): Boolean {
        if (edtFirstName.text.toString().trim().isEmpty()) {
            layoutFirstName.error = "Required Field!"
            edtFirstName.requestFocus()
            return false
        } else {
            layoutFirstName.isErrorEnabled = false
        }
        return true
    }

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