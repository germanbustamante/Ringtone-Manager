package com.germandebustamante.ringtonemanager.core.model.error

object ErrorBOMother {

    fun unknown(message: String = "An unexpected error occurred.") = ErrorBO.Unknown(message = message)

    fun serverError(code: Int = 500, message: String? = "Internal Server Error") = ErrorBO.Server(code, message)

    fun notFound() = ErrorBO.NotFound

    fun parcelizeException() = ErrorBO.ParcelizeException

    fun emailAddressAlreadyInUse() = ErrorBO.EmailAddressAlreadyInUse

    fun invalidCredentials() = ErrorBO.InvalidCredentials
}