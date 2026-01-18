package com.germandebustamante.ringtonemanager.core.model.error

sealed interface ErrorBO {
    data class Server(val code: Int, val message: String?) : ErrorBO
    data object ParcelizeException : ErrorBO
    data object NotFound : ErrorBO
    data object EmailAddressAlreadyInUse : ErrorBO
    data object InvalidCredentials : ErrorBO
    data class Unknown(val message: String? = null) : ErrorBO
}