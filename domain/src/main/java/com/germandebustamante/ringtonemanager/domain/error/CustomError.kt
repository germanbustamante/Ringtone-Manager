package com.germandebustamante.ringtonemanager.domain.error

sealed interface CustomError {
    class Server(val code: Int, val message: String?) : CustomError
    data object ParcelizeException: CustomError
    data object NotFound: CustomError
    class Unknown(val message: String? = null) : CustomError
}