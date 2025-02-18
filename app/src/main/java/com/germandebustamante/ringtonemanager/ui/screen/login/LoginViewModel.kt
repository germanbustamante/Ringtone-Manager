package com.germandebustamante.ringtonemanager.ui.screen.login

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.GetUserFlowUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignInUserUseCase
import com.germandebustamante.ringtonemanager.ui.base.BaseViewModel
import com.germandebustamante.ringtonemanager.ui.base.ValidatorInputState
import com.germandebustamante.ringtonemanager.utils.extensions.collectEither
import com.germandebustamante.ringtonemanager.utils.extensions.isValidEmail
import com.germandebustamante.ringtonemanager.utils.extensions.isValidPassword

class LoginViewModel(
    private val signInUserUseCase: SignInUserUseCase,
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

    fun onSignInButtonClicked() {
        launchCatching {
            if (state.inputsAreValid()) {
                notifyLoading(true)
                signInUserUseCase(
                    email = state.email.value,
                    password = state.password.value,
                )?.let { state = state.copy(loading = false, error = it.toErrorString()) }
            } else {
                updateInputsValidatorState()
            }
        }
    }

    fun onPasswordForgottenClicked() {
//        navigator.navigateToForgotPassword()
    }
    //endregion

    //region Private Methods
    private fun notifyLoading(loading: Boolean) {
        state = state.copy(loading = loading)
    }

    private fun updateInputsValidatorState() {
        val isEmailValid = state.email.value.isValidEmail()
        val isPasswordValid = state.password.value.isValidPassword()

        state = state.copy(
            email = state.email.copy(isValid = isEmailValid),
            password = state.password.copy(isValid = isPasswordValid),
        )
    }

    fun cleanError() {
        state = state.copy(error = null)
    }

    fun onCreateNewAccountClicked() {
        navigateTo(Destination.RegisterScreen, navOptions = {
            popUpTo(Destination.LoginScreen) { inclusive = true }
        })
    }
    //endregion


    data class UIState(
        val email: ValidatorInputState = ValidatorInputState(),
        val password: ValidatorInputState = ValidatorInputState(),
        val error: String? = null,
        val loading: Boolean = false,
    ) {
        fun inputsAreValid(): Boolean = email.value.isValidEmail() && password.value.isValidPassword()
    }
}