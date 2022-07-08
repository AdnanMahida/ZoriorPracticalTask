package com.ad.zoriorpracticaltask.util

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Matcher
import java.util.regex.Pattern


fun validateInput(
    inputEditText: TextInputEditText,
    txtInputLayout: TextInputLayout
): Boolean {
    if (inputEditText.text.toString().trim().isEmpty()) {
        txtInputLayout.error = "Required Field!"
        inputEditText.requestFocus()
        return false
    } else {
        txtInputLayout.isErrorEnabled = false
    }
    return true
}

fun emailValidation(email: String?): Boolean {
    if (null == email || email.isEmpty()) {
        return false
    }
    val emailPattern: Pattern = Pattern
        .compile(
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        )
    val emailMatcher: Matcher = emailPattern.matcher(email)
    return emailMatcher.matches()
}