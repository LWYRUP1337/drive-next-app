package com.example.myapplication.data

import android.content.Context
import androidx.core.content.edit

// Локальное хранилище: пройден ли онбординг и access токен сессии
class SessionStorage(context: Context) {

    private val prefs = context.getSharedPreferences("drive_next", Context.MODE_PRIVATE)

    var isOnboardingShown: Boolean
        get() = prefs.getBoolean(KEY_ONBOARDING, false)
        set(value) = prefs.edit { putBoolean(KEY_ONBOARDING, value) }

    fun saveToken(accessToken: String, expiresAtMillis: Long) = prefs.edit {
        putString(KEY_TOKEN, accessToken)
        putLong(KEY_EXPIRES_AT, expiresAtMillis)
    }

    fun hasValidToken(): Boolean =
        prefs.getString(KEY_TOKEN, null) != null &&
            prefs.getLong(KEY_EXPIRES_AT, 0) > System.currentTimeMillis()

    private companion object {
        const val KEY_ONBOARDING = "onboarding_shown"
        const val KEY_TOKEN = "access_token"
        const val KEY_EXPIRES_AT = "token_expires_at"
    }
}
