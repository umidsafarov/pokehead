package com.umidsafarov.pokehead.common

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : Resource<T>(data = data)
    class Error<T>(message: String? = null, data: T? = null) : Resource<T>(data = data, message = message)
    class Loading<T>(val isLoading: Boolean = true) : Resource<T>(data = null)
}