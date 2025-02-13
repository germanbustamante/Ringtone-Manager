package com.germandebustamante.ringtonemanager.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    private val navigator: Navigator,
) : ViewModel() {

    fun navigateUp() {
        launchCatching {
            navigator.navigateUp()
        }
    }

    fun navigateTo(destination: Destination) {
        launchCatching {
            navigator.navigate(destination)
        }
    }

    fun launchCatching(
        onError: (String) -> Unit = {},
        block: suspend CoroutineScope.() -> Unit,
    ) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
//                Firebase.crashlytics.recordException(throwable)
                onError(throwable.toError().toErrorString())
            },
            block = block
        )

    fun CustomError.toErrorString(): String {
        return when (this) {
            is CustomError.Server -> message ?: "Server error"
            is CustomError.ParcelizeException -> "Parcelize error"
            is CustomError.Unknown -> message ?: "Synchronizing error"
            is CustomError.EmailAddressAlreadyInUse -> "Email address already in use"
            CustomError.NotFound -> "Item not found"
            CustomError.InvalidCredentials -> "Email or password incorrect"
        }
    }

    private fun Throwable.toError(): CustomError = when (this) {
        is StorageException -> CustomError.Server(errorCode, message)
        is RuntimeException -> CustomError.ParcelizeException
        is FirebaseAuthUserCollisionException -> CustomError.EmailAddressAlreadyInUse  //Invoked when we try to call register with a email that is already in use
        is FirebaseAuthInvalidCredentialsException -> CustomError.InvalidCredentials  //When try to login with a non existent email AND if try login with bad password but user exists
        else -> CustomError.Unknown(message)
    }
}