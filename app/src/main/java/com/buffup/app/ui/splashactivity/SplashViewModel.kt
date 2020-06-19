package com.buffup.app.ui.splashactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buffup.app.model.SignInResult
import com.buffup.sdk.BuffSdk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SplashViewModel : ViewModel() {
    private val _signInResult = MutableLiveData<SignInResult>()
    val signInResult: LiveData<SignInResult> = _signInResult

    fun signInAsGuest() =
        viewModelScope.launch {
            try {
                _signInResult.value = SignInResult.Loading
                withContext(Dispatchers.IO) {
                    BuffSdk.signIn(
                        uuid = UUID.randomUUID().toString(),
                        locale = LANGUAGE_TAG,
                        username = android.os.Build.MODEL
                    )
                }
                _signInResult.value = SignInResult.Success
            } catch (error: Throwable) {
                _signInResult.value = SignInResult.Error(error)
            }
        }

    companion object {
        private const val LANGUAGE_TAG = "en"
    }
}