package com.example.myapplication.ui.registration

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.ui.common.openAsRoot
import com.example.myapplication.ui.main.MainActivity

class RegistrationSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_success)

        findViewById<Button>(R.id.btnNext).setOnClickListener { openAsRoot(MainActivity::class.java) }
    }
}
