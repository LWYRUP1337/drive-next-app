package com.example.myapplication.ui.registration

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.data.AuthRepository
import com.example.myapplication.domain.RegistrationData
import com.example.myapplication.domain.Validator
import com.example.myapplication.ui.common.RequestState
import kotlinx.coroutines.launch

enum class PhotoTarget { AVATAR, LICENSE, PASSPORT }

// Общая для трёх шагов регистрации: хранит введённые данные между экранами
class RegistrationViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = AuthRepository(app)

    private var email = ""
    private var password = ""
    private var lastName = ""
    private var firstName = ""
    private var middleName = ""
    private var birthDate = ""
    private var gender = ""

    val photos = mutableMapOf<PhotoTarget, Uri>()
    var photoTarget: PhotoTarget? = null
    var pendingPhotoUri: Uri? = null

    private val _state = MutableLiveData<RequestState>(RequestState.Idle)
    val state: LiveData<RequestState> = _state

    // Каждый submit возвращает текст ошибки или null, если шаг заполнен верно
    fun submitStep1(email: String, password: String, repeatPassword: String, agreed: Boolean): Int? {
        if (!Validator.isEmailValid(email)) return R.string.error_email
        if (password.isEmpty()) return R.string.error_required
        if (password != repeatPassword) return R.string.error_passwords
        if (!agreed) return R.string.error_agreement
        this.email = email.trim()
        this.password = password
        return null
    }

    fun isStep2Filled(lastName: String, firstName: String, birthDate: String, gender: String?) =
        lastName.isNotBlank() && firstName.isNotBlank() && birthDate.isNotBlank() && gender != null

    fun submitStep2(lastName: String, firstName: String, middleName: String, birthDate: String, gender: String?): Int? {
        if (!isStep2Filled(lastName, firstName, birthDate, gender)) return R.string.error_required
        if (!Validator.isBirthDateValid(birthDate)) return R.string.error_birth_date
        this.lastName = lastName.trim()
        this.firstName = firstName.trim()
        this.middleName = middleName.trim()
        this.birthDate = birthDate
        this.gender = gender.orEmpty()
        return null
    }

    fun isStep3Filled(licenseNumber: String, issueDate: String) =
        licenseNumber.isNotBlank() && issueDate.isNotBlank()

    fun register(licenseNumber: String, issueDate: String): Int? {
        if (!isStep3Filled(licenseNumber, issueDate)) return R.string.error_required
        if (!Validator.isLicenseNumberValid(licenseNumber)) return R.string.error_license
        if (!Validator.isIssueDateValid(issueDate)) return R.string.error_issue_date
        if (PhotoTarget.LICENSE !in photos || PhotoTarget.PASSPORT !in photos) return R.string.error_photos

        val data = RegistrationData(
            email, password, lastName, firstName, middleName, birthDate, gender,
            licenseNumber.trim(), issueDate
        )
        _state.value = RequestState.Loading
        viewModelScope.launch {
            _state.value = repository.signUp(data).fold(
                onSuccess = { RequestState.Success },
                onFailure = { RequestState.Error(it.message ?: "Неизвестная ошибка") }
            )
        }
        return null
    }

    fun errorShown() {
        _state.value = RequestState.Idle
    }
}
