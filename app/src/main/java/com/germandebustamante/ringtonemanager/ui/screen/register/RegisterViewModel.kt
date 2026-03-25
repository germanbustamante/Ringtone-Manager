package com.germandebustamante.ringtonemanager.ui.screen.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.germandebustamante.ringtonemanager.core.model.authorization.LoginTypeBO
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.GetUserFlowUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignInUserUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignUpUserUseCase
import com.germandebustamante.ringtonemanager.ui.base.BaseViewModel
import com.germandebustamante.ringtonemanager.ui.base.ValidatorInputState
import com.germandebustamante.ringtonemanager.utils.extensions.collectEither
import com.germandebustamante.ringtonemanager.utils.extensions.isValidEmail
import com.germandebustamante.ringtonemanager.utils.extensions.isValidPassword

class RegisterViewModel(
    private val signUpUserUseCase: SignUpUserUseCase,
    private val currentUserFlowUseCase: GetUserFlowUseCase,
    private val signInUserUseCase: SignInUserUseCase,
    navigator: Navigator,
) : BaseViewModel(navigator) {

    var state by mutableStateOf(UIState())
        private set

    init {
        launchCatching(onError = { notifyError(it) }) {
            currentUserFlowUseCase().collectEither(
                onLeft = { notifyError(it) },
                onRight = { if (it != null) navigateUp() }
            )
        }
    }

    //region Public Methods
    fun updateEmail(email: String) {
        state = state.copy(email = state.email.copy(value = email))
    }

    fun updateName(name: String) {
        state = state.copy(name = state.name.copy(value = name))
    }

    fun updatePassword(password: String) {
        state = state.copy(password = state.password.copy(value = password))
    }

    fun updateRepeatPassword(repeatPassword: String) {
        state = state.copy(repeatPassword = state.repeatPassword.copy(value = repeatPassword))
    }

    fun onSignUpButtonClicked() {
        launchCatching(onError = { notifyError(it) }) {
            if (state.inputsAreValid()) {
                notifyLoading()
                signUpUserUseCase(
                    email = state.email.value,
                    password = state.password.value,
                    name = state.name.value
                ).fold(
                    ifLeft = { state = state.copy(loading = false, error = it) },
                    ifRight = { state = state.copy(loading = false) }
                )
            } else {
                updateInputsValidatorState()
            }
        }
    }
    //endregion

    //region Private Methods
    private fun notifyLoading() {
        state = state.copy(loading = true)
    }

    private fun notifyError(error: ErrorBO) {
        state = state.copy(loading = false, error = error)
    }

    private fun updateInputsValidatorState() {
        val isEmailValid = state.email.value.isValidEmail()
        val isNameValid = state.name.value.isNotBlank()
        val isPasswordValid = state.password.value.isValidPassword()
        val isRepeatPasswordValid = state.repeatPassword.value == state.password.value

        state = state.copy(
            email = state.email.copy(isValid = isEmailValid),
            name = state.name.copy(isValid = isNameValid),
            password = state.password.copy(isValid = isPasswordValid),
            repeatPassword = state.repeatPassword.copy(isValid = isRepeatPasswordValid)
        )
    }

    fun cleanError() {
        state = state.copy(error = null)
    }

    fun navigateToLogin() {
        navigateTo(Destination.LoginScreen)
    }

    fun onGoogleIdTokenReceived(googleTokenId: String) {
        launchCatching(onError = { notifyError(it) }) {
            notifyLoading()
            signInUserUseCase(LoginTypeBO.Google(googleTokenId)).fold(
                ifLeft = { state = state.copy(loading = false, error = it) },
                ifRight = { state = state.copy(loading = false) }
            )
        }
    }
    //endregion

    data class UIState(
        val email: ValidatorInputState = ValidatorInputState(),
        val name: ValidatorInputState = ValidatorInputState(),
        val password: ValidatorInputState = ValidatorInputState(),
        val repeatPassword: ValidatorInputState = ValidatorInputState(),
        val error: ErrorBO? = null,
        val loading: Boolean = false,
    ) {
        fun inputsAreValid(): Boolean = email.value.isValidEmail() &&
            name.value.isNotBlank() &&
            password.value.isValidPassword() &&
            repeatPassword.value == password.value
    }
}
