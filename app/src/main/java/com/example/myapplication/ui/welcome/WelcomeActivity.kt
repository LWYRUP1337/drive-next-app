package com.example.myapplication.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.ui.login.LoginActivity
import com.example.myapplication.ui.registration.RegistrationActivity

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        findViewById<Button>(R.id.btnSignIn).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        findViewById<Button>(R.id.btnSignUp).setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}
