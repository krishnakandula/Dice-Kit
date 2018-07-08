package com.apps.krishnakandula.common.util

sealed class Result<out T> {
    class Success<out T>(val data: T) : Result<T>()
    class Error<out T>(val error: Throwable) : Result<T>()
}
