package com.example.myapplication.ui.registration

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.ui.common.pickDate
import com.example.myapplication.ui.common.showError
import com.google.android.material.textfield.TextInputLayout

class Step2Fragment : Fragment(R.layout.fragment_step2) {

    private val viewModel: RegistrationViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etLastName = view.findViewById<EditText>(R.id.etLastName)
        val etFirstName = view.findViewById<EditText>(R.id.etFirstName)
        val etMiddleName = view.findViewById<EditText>(R.id.etMiddleName)
        val tilBirthDate = view.findViewById<TextInputLayout>(R.id.tilBirthDate)
        val etBirthDate = view.findViewById<EditText>(R.id.etBirthDate)
        val rgGender = view.findViewById<RadioGroup>(R.id.rgGender)
        val btnNext = view.findViewById<Button>(R.id.btnNext)

        fun gender() = when (rgGender.checkedRadioButtonId) {
            R.id.rbMale -> "male"
            R.id.rbFemale -> "female"
            else -> null
        }

        // «Далее» активна, только когда заполнены обязательные поля
        val updateButton = {
            btnNext.isEnabled = viewModel.isStep2Filled(
                etLastName.text.toString(), etFirstName.text.toString(), etBirthDate.text.toString(), gender()
            )
        }
        etLastName.doAfterTextChanged { updateButton() }
        etFirstName.doAfterTextChanged { updateButton() }
        etBirthDate.doAfterTextChanged { updateButton() }
        rgGender.setOnCheckedChangeListener { _, _ -> updateButton() }
        updateButton()

        val openDatePicker = { parentFragmentManager.pickDate { etBirthDate.setText(it) } }
        tilBirthDate.setStartIconOnClickListener { openDatePicker() }
        etBirthDate.setOnClickListener { openDatePicker() }

        btnNext.setOnClickListener {
            val error = viewModel.submitStep2(
                etLastName.text.toString(),
                etFirstName.text.toString(),
                etMiddleName.text.toString(),
                etBirthDate.text.toString(),
                gender()
            )
            if (error != null) showError(error) else (requireActivity() as RegistrationActivity).openStep(Step3Fragment())
        }
    }
}
