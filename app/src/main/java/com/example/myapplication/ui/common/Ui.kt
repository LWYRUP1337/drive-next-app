package com.example.myapplication.ui.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapplication.R
import com.example.myapplication.domain.Validator
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
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

fun Activity.openAsRoot(screen: Class<out Activity>) {
    startActivity(Intent(this, screen).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
}

fun Context.loadBitmap(uri: Uri): Bitmap? = runCatching {
    ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri)) { decoder, info, _ ->
        decoder.setTargetSampleSize(maxOf(1, maxOf(info.size.width, info.size.height) / 512))
    }
}.getOrNull()

fun ImageView.showAvatar(file: File?, placeholder: Int) {
    val bitmap = file?.let { context.loadBitmap(Uri.fromFile(it)) }
    if (bitmap != null) {
        scaleType = ImageView.ScaleType.CENTER_CROP
        setImageBitmap(bitmap)
    } else {
        scaleType = ImageView.ScaleType.CENTER_INSIDE
        setImageResource(placeholder)
    }
}

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
