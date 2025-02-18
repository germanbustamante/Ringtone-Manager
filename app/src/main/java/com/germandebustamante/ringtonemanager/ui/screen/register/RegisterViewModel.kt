package com.germandebustamante.ringtonemanager.ui.screen.register

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.GetUserFlowUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignUpUserUseCase
import com.germandebustamante.ringtonemanager.ui.base.BaseViewModel
import com.germandebustamante.ringtonemanager.ui.base.ValidatorInputState
import com.germandebustamante.ringtonemanager.utils.extensions.collectEither
import com.germandebustamante.ringtonemanager.utils.extensions.isValidEmail
import com.germandebustamante.ringtonemanager.utils.extensions.isValidPassword

class RegisterViewModel(
    private val signUpUserUseCase: SignUpUserUseCase,
    private val currentUserFlowUseCase: GetUserFlowUseCase,
    navigator: Navigator,
    context: Context,
) : BaseViewModel(navigator, context) {

    var state by mutableStateOf(UIState())
        private set

    init {
        launchCatching {
            currentUserFlowUseCase().collectEither(
                onLeft = { state = state.copy(loading = false, error = it.toErrorString()) },
                onRight = { if (it != null) navigateUp() }
            )
        }
    }

    //region Public Methods
    fun updateEmail(email: String) {
        state = state.copy(email = state.email.copy(value = email))
    }

    fun updatePassword(password: String) {
        state = state.copy(password = state.password.copy(value = password))
    }

    fun updateRepeatPassword(repeatPassword: String) {
        state = state.copy(repeatPassword = state.repeatPassword.copy(value = repeatPassword))
    }

    fun onSignInButtonClicked() {
        launchCatching {
            if (state.inputsAreValid()) {
                notifyLoading(true)
                signUpUserUseCase(
                    email = state.email.value,
                    password = state.password.value
                )?.let { state = state.copy(loading = false, error = it.toErrorString()) }
            } else {
                updateInputsValidatorState()
            }
        }
    }
    //endregion

    //region Private Methods
    private fun notifyLoading(loading: Boolean) {
        state = state.copy(loading = loading)
    }

    private fun updateInputsValidatorState() {
        val isEmailValid = state.email.value.isValidEmail()
        val isPasswordValid = state.password.value.isValidPassword()
        val isRepeatPasswordValid = state.repeatPassword.value == state.password.value

        state = state.copy(
            email = state.email.copy(isValid = isEmailValid),
            password = state.password.copy(isValid = isPasswordValid),
            repeatPassword = state.repeatPassword.copy(isValid = isRepeatPasswordValid)
        )
    }

    fun cleanError() {
        state = state.copy(error = null)
    }

    fun navigateToLogin() {
        navigateTo(Destination.LoginScreen, navOptions = {
            popUpTo(Destination.RegisterScreen) { inclusive = true }
        })
    }
    //endregion

    data class UIState(
        val email: ValidatorInputState = ValidatorInputState(),
        val password: ValidatorInputState = ValidatorInputState(),
        val repeatPassword: ValidatorInputState = ValidatorInputState(),
        val error: String? = null,
        val loading: Boolean = false,
    ) {
        fun inputsAreValid(): Boolean =
            email.value.isValidEmail()
                    && password.value.isValidPassword()
                    && repeatPassword.value == password.value
    }
}