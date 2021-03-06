package com.ad.zoriorpracticaltask.util

import android.app.Activity
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.database.Cursor
import android.icu.util.Calendar
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.Fragment
import com.ad.zoriorpracticaltask.R
import com.ad.zoriorpracticaltask.data.network.ResultState
import com.ad.zoriorpracticaltask.ui.authentication.login.LoginFragment
import com.google.android.material.snackbar.Snackbar


fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

fun View.enable(enable: Boolean) {
    isEnabled = enable
    alpha = if (enable) 1f else 0.5f
}

fun View.snackBar(
    message: String,
    action: (() -> Unit)? = null
) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}

fun setChecked(gender: String): Int {
    return if (gender.lowercase() == "female") R.id.radioFemale
    else R.id.radioMale
}

fun Fragment.handleApiError(
    failure: ResultState.Failure,
    retry: (() -> Unit)? = null
) {

    when {
        failure.isNetworkError -> requireView().snackBar(
            "Please check your internet connection",
            retry
        )
        failure.errorCode == 401 -> {
            if (this is LoginFragment) {
                requireView().snackBar("You've entered incorrect email or password")
            }
        }
        else -> {
            val error = failure.errorBody?.string().toString()
            requireView().snackBar(error)
        }
    }
}


fun getAge(year: Int, month: Int, day: Int): String? {
    val dob: Calendar = Calendar.getInstance()
    val today: Calendar = Calendar.getInstance()
    dob.set(year, month, day)
    var age: Int = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
        age--
    }
    val ageInt = age
    return ageInt.toString()
}

fun Context.getRealPathFromURI(contentUri: Uri): String? {
    val proj = arrayOf(MediaStore.Images.Media.DATA)
    val loader = CursorLoader(this, contentUri, proj, null, null, null)
    val cursor: Cursor = loader.loadInBackground()
    val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor.moveToFirst()
    val result: String = cursor.getString(column_index)
    cursor.close()
    return result
}
