package com.evsyuk.quizconstructor.utils

sealed class DatabaseResponse<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?, message: String?) : DatabaseResponse<T>(data, message = message)
    class Failure<T>(message: String?) : DatabaseResponse<T>(message = message)
    class Loading<T>() : DatabaseResponse<T>()
}
