package com.example.myapplication.ui.noconnection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.ui.splash.Destination
import com.example.myapplication.ui.splash.StartViewModel

class NoConnectionActivity : AppCompatActivity() {

    private val viewModel: StartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_connection)

        // Если сети всё ещё нет — остаёмся на этом экране
        viewModel.destination.observe(this) { destination ->
            if (destination != Destination.NO_CONNECTION) {
                startActivity(Intent(this, destination.screen))
                finish()
            }
        }
        findViewById<Button>(R.id.btnRetry).setOnClickListener { viewModel.start() }
    }
}
