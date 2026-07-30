package com.germandebustamante.ringtonemanager.ui.screen.login

import com.germandebustamante.ringtonemanager.core.model.authorization.LoginTypeBO
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.GetUserFlowUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignInUserUseCase
import com.germandebustamante.ringtonemanager.ui.base.BaseViewModel
import com.germandebustamante.ringtonemanager.ui.base.ValidatorInputState
import com.germandebustamante.ringtonemanager.utils.extensions.collectEither
import com.germandebustamante.ringtonemanager.utils.extensions.isValidEmail
import com.germandebustamante.ringtonemanager.utils.extensions.isValidPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel(
    initialState: UIState = UIState(),
    private val signInUserUseCase: SignInUserUseCase,
    private val currentUserFlowUseCase: GetUserFlowUseCase,
    navigator: Navigator,
) : BaseViewModel(navigator) {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<UIState> = _state

    fun start() {
        launchCatching(onError = { notifyError(it) }) {
            currentUserFlowUseCase().collectEither(
                onLeft = { notifyError(it) },
                onRight = { if (it != null) navigateUp() }
            )
        }
    }

    //region Public Methods
    fun updateCredentials(email: String, password: String) {
        _state.update {
            it.copy(
                email = it.email.copy(value = email),
                password = it.password.copy(value = password),
            )
        }
    }

    fun updateEmail(email: String) {
        _state.update { it.copy(email = it.email.copy(value = email)) }
    }

    fun updatePassword(password: String) {
        _state.update { it.copy(password = it.password.copy(value = password)) }
    }

    fun onSignInButtonClicked() {
        launchCatching(onError = { notifyError(it) }) {
            if (_state.value.inputsAreValid()) {
                notifyLoading()
                signInUserUseCase(
                    LoginTypeBO.Default(
                        email = _state.value.email.value,
                        password = _state.value.password.value
                    )
                ).fold(
                    ifLeft = { _state.update { s -> s.copy(loading = false, error = it) } },
                    ifRight = { _state.update { s -> s.copy(loading = false) } }
                )
            } else {
                updateInputsValidatorState()
            }
        }
    }

    fun onPasswordForgottenClicked() {
        navigateTo(Destination.ForgotPasswordScreen)
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
                password = it.password.copy(isValid = it.password.value.isValidPassword()),
            )
        }
    }

    fun cleanError() {
        _state.update { it.copy(error = null) }
    }

    fun onCreateNewAccountClicked() {
        navigateTo(Destination.RegisterScreen)
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
        val password: ValidatorInputState = ValidatorInputState(),
        val error: ErrorBO? = null,
        val loading: Boolean = false,
    ) {
        fun inputsAreValid(): Boolean = email.value.isValidEmail() && password.value.isValidPassword()
    }
}
