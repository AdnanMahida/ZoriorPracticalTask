package com.ad.zoriorpracticaltask.data

import android.content.Context
import android.content.SharedPreferences
import com.ad.zoriorpracticaltask.data.response.User


object AppPreferences {
    private const val NAME = "com.ad.zoriorpracticaltask.pref"
    private const val USER_ID = "UserId"
    private const val TOKEN = "Token"

    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var userId: Int?
        get() = preferences.getInt(USER_ID, 0)
        set(value) = preferences.edit {
            it.putInt(USER_ID, value ?: 0)
        }


    var token: String?
        get() = preferences.getString(TOKEN, null)
        set(value) = preferences.edit {
            it.putString(TOKEN, value)
        }

    fun clearPref() {
        preferences.edit().clear().apply()
    }

    fun saveUser(user: User) {
        userId = Integer.parseInt(user.userId ?: "0")
        token = user.activationToken
    }
}