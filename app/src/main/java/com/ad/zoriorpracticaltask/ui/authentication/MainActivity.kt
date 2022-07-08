package com.ad.zoriorpracticaltask.ui.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ad.zoriorpracticaltask.data.AppPreferences
import com.ad.zoriorpracticaltask.databinding.ActivityMainBinding
import com.ad.zoriorpracticaltask.ui.home.HomeActivity
import com.ad.zoriorpracticaltask.util.startNewActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (AppPreferences.token != null) {
            startNewActivity(HomeActivity::class.java)
        }

    }

}