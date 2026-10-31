package com.example.myapplication.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.myapplication.R

class SplashActivity : AppCompatActivity() {

    private val viewModel: StartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel.destination.observe(this) { destination ->
            startActivity(Intent(this, destination.screen))
            finish()
        }
        if (savedInstanceState == null) viewModel.start(delayMillis = 2500)
    }
}
