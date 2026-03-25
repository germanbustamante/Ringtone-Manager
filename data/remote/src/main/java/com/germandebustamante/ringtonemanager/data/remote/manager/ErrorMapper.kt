package com.germandebustamante.ringtonemanager.data.remote.manager

import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.storage.StorageException

fun Throwable.toErrorBO(): ErrorBO = when (this) {
    is StorageException -> ErrorBO.Server(errorCode, message)
    is FirebaseAuthUserCollisionException -> ErrorBO.EmailAddressAlreadyInUse
    is FirebaseAuthInvalidCredentialsException -> ErrorBO.InvalidCredentials
    else -> ErrorBO.Unknown(message)
}
