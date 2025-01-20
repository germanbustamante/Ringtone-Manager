package com.germandebustamante.ringtonemanager.ui.base

import androidx.lifecycle.ViewModel
import com.germandebustamante.ringtonemanager.domain.error.CustomError

abstract class BaseViewModel: ViewModel() {

    fun CustomError.toErrorString(): String {
        return when (this) {
            is CustomError.Server -> message ?: "Server error"
            is CustomError.ParcelizeException -> "Parcelize error"
            is CustomError.Unknown -> message ?: "Synchronizing error"
        }
    }
}