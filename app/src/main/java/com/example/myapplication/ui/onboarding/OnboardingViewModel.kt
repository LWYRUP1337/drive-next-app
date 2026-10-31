package com.example.myapplication.ui.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.data.SessionStorage

class OnboardingViewModel(app: Application) : AndroidViewModel(app) {

    private val storage = SessionStorage(app)

    // Запоминаем, что онбординг пройден, чтобы не показывать его повторно
    fun finishOnboarding() {
        storage.isOnboardingShown = true
    }
}
