package com.example.myapplication.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.myapplication.R
import com.example.myapplication.ui.common.RequestState
import com.example.myapplication.ui.common.openAsRoot
import com.example.myapplication.ui.common.showError
import com.example.myapplication.ui.main.MainActivity
import com.example.myapplication.ui.registration.RegistrationActivity

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val progress = findViewById<View>(R.id.progress)

        // «Войти» активна, только когда заполнены все поля
        val updateButton = {
            btnSignIn.isEnabled = viewModel.canSignIn(etEmail.text.toString(), etPassword.text.toString())
        }
        etEmail.doAfterTextChanged { updateButton() }
        etPassword.doAfterTextChanged { updateButton() }

        btnSignIn.setOnClickListener {
            viewModel.signIn(etEmail.text.toString(), etPassword.text.toString())
                ?.let { showError(getString(it)) }
        }
        findViewById<TextView>(R.id.tvSignUp).setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        viewModel.state.observe(this) { state ->
            val loading = state == RequestState.Loading
            progress.visibility = if (loading) View.VISIBLE else View.GONE
            if (loading) btnSignIn.isEnabled = false else updateButton()

            when (state) {
                RequestState.Success -> openAsRoot(MainActivity::class.java)
                is RequestState.Error -> {
                    showError(state.message)
                    viewModel.errorShown()
                }
                else -> Unit
            }
        }
    }
}
