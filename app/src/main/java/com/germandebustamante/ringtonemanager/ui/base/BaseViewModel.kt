package com.germandebustamante.ringtonemanager.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    private val navigator: Navigator,
) : ViewModel() {

    fun navigateUp() {
        launchCatching(onError = {}) {
            navigator.navigateUp()
        }
    }

    fun navigateTo(destination: Destination) {
        viewModelScope.launch {
            navigator.navigate(destination)
        }
    }

    fun launchCatching(
        onError: (ErrorBO) -> Unit,
        block: suspend CoroutineScope.() -> Unit,
    ) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                onError(ErrorBO.Unknown(throwable.message))
            },
            block = block
        )
}
