package com.example.myapplication.ui.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapplication.R
import com.example.myapplication.domain.Validator
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.Instant
import java.time.ZoneOffset

fun Context.showError(message: String) {
    MaterialAlertDialogBuilder(this)
        .setTitle(R.string.error_title)
        .setMessage(message)
        .setPositiveButton(R.string.ok, null)
        .show()
}

fun Fragment.showError(messageRes: Int) = requireContext().showError(getString(messageRes))

// Открывает экран и очищает стек навигации
fun Activity.openAsRoot(screen: Class<out Activity>) {
    startActivity(Intent(this, screen).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
}

// Выбор даты через MaterialDatePicker, результат в формате DD/MM/YYYY
fun FragmentManager.pickDate(onPicked: (String) -> Unit) {
    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText(R.string.select_date)
        .build()
    datePicker.addOnPositiveButtonClickListener { selectedDateUtcMillis ->
        val date = Instant.ofEpochMilli(selectedDateUtcMillis).atZone(ZoneOffset.UTC).toLocalDate()
        onPicked(Validator.dateFormatter.format(date))
    }
    datePicker.show(this, "date_picker")
}
