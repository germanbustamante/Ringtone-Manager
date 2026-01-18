package com.germandebustamante.ringtonemanager.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
        viewModelScope.launch(Dispatchers.IO) {
            navigator.navigate(destination)
        }
    }

    fun launchCatching(
        onError: (ErrorBO) -> Unit = {},
        block: suspend CoroutineScope.() -> Unit,
    ) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
//                Firebase.crashlytics.recordException(throwable)
                onError(throwable.toError())
            },
            block = block
        )

    private fun Throwable.toError(): ErrorBO = when (this) {
        is StorageException -> ErrorBO.Server(errorCode, message)
        is RuntimeException -> ErrorBO.ParcelizeException
        // Invoked when we try to call register with a email that is already in use
        is FirebaseAuthUserCollisionException -> ErrorBO.EmailAddressAlreadyInUse
        // When try to login with a non existent email AND if try login with bad password but user exists
        is FirebaseAuthInvalidCredentialsException -> ErrorBO.InvalidCredentials
        else -> ErrorBO.Unknown(message)
    }
}