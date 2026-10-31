package com.example.myapplication.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.data.AuthRepository
import com.example.myapplication.domain.Validator
import com.example.myapplication.ui.common.RequestState
import kotlinx.coroutines.launch

class LoginViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = AuthRepository(app)

    private val _state = MutableLiveData<RequestState>(RequestState.Idle)
    val state: LiveData<RequestState> = _state

    fun canSignIn(email: String, password: String) = email.isNotBlank() && password.isNotEmpty()

    // Возвращает текст ошибки проверки полей или null, если запрос отправлен
    fun signIn(email: String, password: String): Int? {
        if (!Validator.isEmailValid(email)) return R.string.error_email
        _state.value = RequestState.Loading
        viewModelScope.launch {
            _state.value = repository.signIn(email.trim(), password).fold(
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
