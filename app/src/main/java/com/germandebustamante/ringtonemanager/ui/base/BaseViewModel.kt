package com.germandebustamante.ringtonemanager.ui.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptionsBuilder
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    private val navigator: Navigator,
    private val context: Context //TODO GBC MEJORAR ESTO
) : ViewModel() {

    fun navigateUp() {
        launchCatching {
            navigator.navigateUp()
        }
    }

    fun navigateTo(destination: Destination, navOptions: NavOptionsBuilder.() -> Unit = {}) {
        viewModelScope.launch(Dispatchers.IO) {
            navigator.navigate(destination, navOptions)
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
            is CustomError.Server -> message ?: context.getString(R.string.server_error)
            is CustomError.ParcelizeException -> context.getString(R.string.parcelize_error)
            is CustomError.Unknown -> message ?: context.getString(R.string.unknown_error)
            is CustomError.EmailAddressAlreadyInUse -> context.getString(R.string.email_address_already_in_use)
            CustomError.NotFound -> context.getString(R.string.item_not_found)
            CustomError.InvalidCredentials -> context.getString(R.string.email_or_password_incorrect)
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