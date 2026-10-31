package com.example.myapplication.ui.registration

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.ui.common.pickDate
import com.example.myapplication.ui.common.showError
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputLayout
import java.io.File

class Step3Fragment : Fragment(R.layout.fragment_step3) {

    private val viewModel: RegistrationViewModel by activityViewModels()

    // Выбор одной фотографии из галереи
    private val pickOneImage = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) onPhotoSelected(uri)
    }

    // Снимок с камеры: камера сохраняет фото в заранее подготовленный Uri
    private val takeOnePhoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        val uri = viewModel.pendingPhotoUri
        if (success && uri != null) onPhotoSelected(uri)
    }

    private lateinit var photoViews: Map<PhotoTarget, ShapeableImageView>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etLicense = view.findViewById<EditText>(R.id.etLicense)
        val tilIssueDate = view.findViewById<TextInputLayout>(R.id.tilIssueDate)
        val etIssueDate = view.findViewById<EditText>(R.id.etIssueDate)
        val btnNext = view.findViewById<Button>(R.id.btnNext)

        photoViews = mapOf(
            PhotoTarget.AVATAR to view.findViewById(R.id.ivAvatar),
            PhotoTarget.LICENSE to view.findViewById(R.id.ivLicensePhoto),
            PhotoTarget.PASSPORT to view.findViewById(R.id.ivPassportPhoto)
        )
        view.findViewById<View>(R.id.avatar).setOnClickListener { choosePhoto(PhotoTarget.AVATAR) }
        view.findViewById<View>(R.id.licensePhoto).setOnClickListener { choosePhoto(PhotoTarget.LICENSE) }
        view.findViewById<View>(R.id.passportPhoto).setOnClickListener { choosePhoto(PhotoTarget.PASSPORT) }
        viewModel.photos.forEach { (target, uri) -> showPhoto(target, uri) }

        // «Далее» активна, только когда заполнены обязательные поля
        val updateButton = {
            btnNext.isEnabled = viewModel.isStep3Filled(etLicense.text.toString(), etIssueDate.text.toString())
        }
        etLicense.doAfterTextChanged { updateButton() }
        etIssueDate.doAfterTextChanged { updateButton() }
        updateButton()

        val openDatePicker = { parentFragmentManager.pickDate { etIssueDate.setText(it) } }
        tilIssueDate.setStartIconOnClickListener { openDatePicker() }
        etIssueDate.setOnClickListener { openDatePicker() }

        btnNext.setOnClickListener {
            viewModel.register(etLicense.text.toString(), etIssueDate.text.toString())
                ?.let { showError(it) }
        }
    }

    private fun choosePhoto(target: PhotoTarget) {
        viewModel.photoTarget = target
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.add_photo)
            .setItems(arrayOf(getString(R.string.gallery), getString(R.string.camera))) { _, which ->
                if (which == 0) openGalleryForOneImage() else openCameraForOnePhoto()
            }
            .show()
    }

    private fun openGalleryForOneImage() {
        pickOneImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun openCameraForOnePhoto() {
        val file = File(requireContext().cacheDir, "images/photo_${System.currentTimeMillis()}.jpg")
        file.parentFile?.mkdirs()
        val outputUri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", file)
        viewModel.pendingPhotoUri = outputUri
        takeOnePhoto.launch(outputUri)
    }

    private fun onPhotoSelected(uri: Uri) {
        val target = viewModel.photoTarget ?: return
        viewModel.photos[target] = uri
        showPhoto(target, uri)
    }

    private fun showPhoto(target: PhotoTarget, uri: Uri) {
        val imageView = photoViews.getValue(target)
        // Уменьшаем фото при декодировании, чтобы большой снимок не занял всю память
        val bitmap = runCatching {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, uri)) { decoder, info, _ ->
                decoder.setTargetSampleSize(maxOf(1, maxOf(info.size.width, info.size.height) / 512))
            }
        }.getOrNull() ?: return
        imageView.setContentPadding(0, 0, 0, 0)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageBitmap(bitmap)
    }
}
