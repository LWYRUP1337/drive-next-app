package com.example.myapplication.ui.common

// Состояние запроса к серверу для экранов входа и регистрации
sealed class RequestState {
    object Idle : RequestState()
    object Loading : RequestState()
    object Success : RequestState()
    data class Error(val message: String) : RequestState()
}
