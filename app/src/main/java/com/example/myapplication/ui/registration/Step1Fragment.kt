package com.example.myapplication.ui.registration

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.ui.common.showError

class Step1Fragment : Fragment(R.layout.fragment_step1) {

    private val viewModel: RegistrationViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val etRepeatPassword = view.findViewById<EditText>(R.id.etRepeatPassword)
        val cbAgreement = view.findViewById<CheckBox>(R.id.cbAgreement)

        view.findViewById<Button>(R.id.btnNext).setOnClickListener {
            val error = viewModel.submitStep1(
                etEmail.text.toString(),
                etPassword.text.toString(),
                etRepeatPassword.text.toString(),
                cbAgreement.isChecked
            )
            if (error != null) showError(error) else (requireActivity() as RegistrationActivity).openStep(Step2Fragment())
        }
    }
}
