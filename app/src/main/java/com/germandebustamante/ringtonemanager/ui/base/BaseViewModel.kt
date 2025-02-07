package com.germandebustamante.ringtonemanager.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

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
            CustomError.NotFound -> "Item not found"
        }
    }

    private fun Throwable.toError(): CustomError = when (this) {
        is StorageException -> CustomError.Server(errorCode, message)
        is RuntimeException -> CustomError.ParcelizeException
        else -> CustomError.Unknown(message)
    }
}