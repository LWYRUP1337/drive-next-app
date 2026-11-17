package com.example.myapplication.ui.profile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.ProfileRepository
import com.example.myapplication.domain.UserProfile
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = ProfileRepository(app)

    private val _profile = MutableLiveData<UserProfile>()
    val profile: LiveData<UserProfile> = _profile

    private val _avatar = MutableLiveData<File?>(repository.avatar())
    val avatar: LiveData<File?> = _avatar

    private val _signedOut = MutableLiveData(false)
    val signedOut: LiveData<Boolean> = _signedOut

    init {
        viewModelScope.launch {
            repository.getProfile().onSuccess { _profile.value = it }
        }
    }

    fun changeAvatar(uri: Uri) {
        repository.saveAvatar(uri).onSuccess { _avatar.value = it }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
            _signedOut.value = true
        }
    }
}
