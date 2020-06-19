package com.buffup.app.model

sealed class SignInResult {

    object Success : SignInResult()
    object Loading : SignInResult()
    class Error(val error: Throwable? = null) : SignInResult()

}