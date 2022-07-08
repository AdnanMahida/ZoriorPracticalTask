package com.ad.zoriorpracticaltask

import android.app.Application
import android.content.Context
import com.ad.zoriorpracticaltask.data.AppPreferences

class ZoriorPracticalTask : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        AppPreferences.init(this)
    }

    companion object {
        lateinit var appContext: Context
    }
}