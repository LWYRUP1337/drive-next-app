package com.example.myapplication.ui.registration

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.myapplication.R
import com.example.myapplication.ui.common.RequestState
import com.example.myapplication.ui.common.openAsRoot
import com.example.myapplication.ui.common.showError

// Контейнер для трёх шагов регистрации; «Назад» возвращает на предыдущий шаг
class RegistrationActivity : AppCompatActivity() {

    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        if (savedInstanceState == null) {
            supportFragmentManager.commit { replace(R.id.container, Step1Fragment()) }
        }
        findViewById<View>(R.id.btnBack).setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val loading = findViewById<View>(R.id.loading)
        viewModel.state.observe(this) { state ->
            loading.visibility = if (state == RequestState.Loading) View.VISIBLE else View.GONE
            when (state) {
                RequestState.Success -> openAsRoot(RegistrationSuccessActivity::class.java)
                is RequestState.Error -> {
                    showError(state.message)
                    viewModel.errorShown()
                }
                else -> Unit
            }
        }
    }

    fun openStep(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.container, fragment)
            addToBackStack(null)
        }
    }
}
