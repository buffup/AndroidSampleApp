package com.buffup.app.model

sealed class ViewResult<out T> {

    class Content<T>(val content: T) : ViewResult<T>()
    object Loading : ViewResult<Nothing>()
    class Error(val error: Throwable? = null) : ViewResult<Nothing>()

}