package com.germandebustamante.ringtonemanager.ui.screen.forgotpassword

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.ForgotPasswordUseCase
import com.germandebustamante.ringtonemanager.ui.base.BaseViewModel
import com.germandebustamante.ringtonemanager.ui.base.ValidatorInputState
import com.germandebustamante.ringtonemanager.utils.extensions.isValidEmail

class ForgotPasswordViewModel(
    context: Context,
    navigator: Navigator,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
) : BaseViewModel(navigator, context) {

    var state by mutableStateOf(UIState())
        private set

    //region Public Methods
    fun updateEmail(email: String) {
        state = state.copy(email = state.email.copy(value = email))
    }

    fun cleanError() {
        state = state.copy(error = null)
    }

    fun onRestorePasswordClicked() {
        launchCatching {
            if (state.email.value.isValidEmail()) {
                state = state.copy(loading = true)
                forgotPasswordUseCase(email = state.email.value)?.let {
                    state = state.copy(loading = false, error = it.toErrorString())
                } ?: run { state = state.copy(loading = false, isEmailSent = true) }
            } else {
                updateInputsValidatorState()
            }
        }
    }
    //endregion


    //region Private Methods
    private fun updateInputsValidatorState() {
        val isEmailValid = state.email.value.isValidEmail()
        state = state.copy(email = state.email.copy(isValid = isEmailValid))
    }
    //endregion


    data class UIState(
        val email: ValidatorInputState = ValidatorInputState(),
        val isEmailSent: Boolean = false,
        val loading: Boolean = false,
        val error: String? = null,
    )
}