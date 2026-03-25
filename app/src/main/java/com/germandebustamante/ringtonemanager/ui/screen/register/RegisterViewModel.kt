package com.germandebustamante.ringtonemanager.ui.screen.register

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class RegisterViewModel(
    private val signUpUserUseCase: SignUpUserUseCase,
    private val currentUserFlowUseCase: GetUserFlowUseCase,
    private val signInUserUseCase: SignInUserUseCase,
    navigator: Navigator,
) : BaseViewModel(navigator) {

    private val _state = MutableStateFlow(UIState())
    val state: StateFlow<UIState> = _state

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
        _state.update { it.copy(email = it.email.copy(value = email)) }
    }

    fun updateName(name: String) {
        _state.update { it.copy(name = it.name.copy(value = name)) }
    }

    fun updatePassword(password: String) {
        _state.update { it.copy(password = it.password.copy(value = password)) }
    }

    fun updateRepeatPassword(repeatPassword: String) {
        _state.update { it.copy(repeatPassword = it.repeatPassword.copy(value = repeatPassword)) }
    }

    fun onSignUpButtonClicked() {
        launchCatching(onError = { notifyError(it) }) {
            if (_state.value.inputsAreValid()) {
                notifyLoading()
                signUpUserUseCase(
                    email = _state.value.email.value,
                    password = _state.value.password.value,
                    name = _state.value.name.value
                ).fold(
                    ifLeft = { _state.update { s -> s.copy(loading = false, error = it) } },
                    ifRight = { _state.update { s -> s.copy(loading = false) } }
                )
            } else {
                updateInputsValidatorState()
            }
        }
    }
    //endregion

    //region Private Methods
    private fun notifyLoading() {
        _state.update { it.copy(loading = true) }
    }

    private fun notifyError(error: ErrorBO) {
        _state.update { it.copy(loading = false, error = error) }
    }

    private fun updateInputsValidatorState() {
        _state.update {
            it.copy(
                email = it.email.copy(isValid = it.email.value.isValidEmail()),
                name = it.name.copy(isValid = it.name.value.isNotBlank()),
                password = it.password.copy(isValid = it.password.value.isValidPassword()),
                repeatPassword = it.repeatPassword.copy(isValid = it.repeatPassword.value == it.password.value),
            )
        }
    }

    fun cleanError() {
        _state.update { it.copy(error = null) }
    }

    fun navigateToLogin() {
        navigateTo(Destination.LoginScreen)
    }

    fun onGoogleIdTokenReceived(googleTokenId: String) {
        launchCatching(onError = { notifyError(it) }) {
            notifyLoading()
            signInUserUseCase(LoginTypeBO.Google(googleTokenId)).fold(
                ifLeft = { _state.update { s -> s.copy(loading = false, error = it) } },
                ifRight = { _state.update { s -> s.copy(loading = false) } }
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
