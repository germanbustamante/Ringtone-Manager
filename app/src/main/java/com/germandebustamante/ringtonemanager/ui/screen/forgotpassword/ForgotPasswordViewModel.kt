package com.germandebustamante.ringtonemanager.ui.screen.forgotpassword

import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.ForgotPasswordUseCase
import com.germandebustamante.ringtonemanager.ui.base.BaseViewModel
import com.germandebustamante.ringtonemanager.ui.base.ValidatorInputState
import com.germandebustamante.ringtonemanager.utils.extensions.isValidEmail
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class ForgotPasswordViewModel(
    navigator: Navigator,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
) : BaseViewModel(navigator) {

    private val _state = MutableStateFlow(UIState())
    val state: StateFlow<UIState> = _state

    private val _emailSentEvent = Channel<Unit>(Channel.BUFFERED)
    val emailSentEvent = _emailSentEvent.receiveAsFlow()

    //region Public Methods
    fun updateEmail(email: String) {
        _state.update { it.copy(email = it.email.copy(value = email)) }
    }

    fun cleanError() {
        _state.update { it.copy(error = null) }
    }

    fun onRestorePasswordClicked() {
        launchCatching(onError = { notifyError(it) }) {
            if (_state.value.email.value.isValidEmail()) {
                _state.update { it.copy(loading = true) }
                forgotPasswordUseCase(email = _state.value.email.value).fold(
                    ifLeft = { _state.update { s -> s.copy(loading = false, error = it) } },
                    ifRight = {
                        _state.update { s -> s.copy(loading = false) }
                        _emailSentEvent.send(Unit)
                    }
                )
            } else {
                updateInputsValidatorState()
            }
        }
    }
    //endregion

    //region Private Methods
    private fun notifyError(error: ErrorBO) {
        _state.update { it.copy(loading = false, error = error) }
    }

    private fun updateInputsValidatorState() {
        _state.update { it.copy(email = it.email.copy(isValid = it.email.value.isValidEmail())) }
    }
    //endregion

    data class UIState(
        val email: ValidatorInputState = ValidatorInputState(),
        val loading: Boolean = false,
        val error: ErrorBO? = null,
    )
}
