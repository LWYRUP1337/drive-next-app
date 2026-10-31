package com.example.myapplication.ui.splash

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.NetworkChecker
import com.example.myapplication.data.SessionStorage
import com.example.myapplication.ui.main.MainActivity
import com.example.myapplication.ui.noconnection.NoConnectionActivity
import com.example.myapplication.ui.onboarding.OnboardingActivity
import com.example.myapplication.ui.welcome.WelcomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class Destination(val screen: Class<out Activity>) {
    NO_CONNECTION(NoConnectionActivity::class.java),
    ONBOARDING(OnboardingActivity::class.java),
    WELCOME(WelcomeActivity::class.java),
    MAIN(MainActivity::class.java)
}

// Решает, куда идти после запуска: используется заставкой и экраном «Нет подключения»
class StartViewModel(app: Application) : AndroidViewModel(app) {

    private val networkChecker = NetworkChecker(app)
    private val storage = SessionStorage(app)

    private val _destination = MutableLiveData<Destination>()
    val destination: LiveData<Destination> = _destination

    fun start(delayMillis: Long = 0) {
        viewModelScope.launch {
            delay(delayMillis)
            _destination.value = when {
                !networkChecker.isOnline() -> Destination.NO_CONNECTION
                !storage.isOnboardingShown -> Destination.ONBOARDING
                storage.hasValidToken() -> Destination.MAIN
                else -> Destination.WELCOME
            }
        }
    }
}
