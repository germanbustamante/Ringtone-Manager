package com.germandebustamante.ringtonemanager.ui.screen.settings

import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.ChangePasswordUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.GetUserFlowUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignOutUserUseCase
import com.germandebustamante.ringtonemanager.ui.base.BaseViewModel
import com.germandebustamante.ringtonemanager.utils.extensions.collectEither
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(
    private val getUserFlowUseCase: GetUserFlowUseCase,
    private val signOutUserUseCase: SignOutUserUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    navigator: Navigator,
) : BaseViewModel(navigator) {

    private val _state = MutableStateFlow(UIState())
    val state: StateFlow<UIState> = _state

    init {
        launchCatching(onError = { _state.update { s -> s.copy(isLoading = false, error = it) } }) {
            getUserFlowUseCase().collectEither(
                onLeft = { _state.update { s -> s.copy(isLoading = false, error = it) } },
                onRight = { _state.update { s -> s.copy(userLogged = it != null, isLoading = false) } },
            )
        }
    }

    fun signOut() {
        launchCatching(onError = {}) {
            signOutUserUseCase()
        }
    }

    fun navigateToSignIn() {
        navigateTo(Destination.LoginScreen)
    }

    fun navigateToSignUp() {
        navigateTo(Destination.RegisterScreen)
    }

    fun cleanError() {
        _state.update { it.copy(error = null) }
    }

    fun showLanguageDialog() {
        _state.update { it.copy(showLanguageDialog = true) }
    }

    fun hideLanguageDialog() {
        _state.update { it.copy(showLanguageDialog = false) }
    }

    fun showChangePasswordDialog() {
        _state.update { it.copy(showChangePasswordDialog = true) }
    }

    fun hideChangePasswordDialog() {
        _state.update { it.copy(showChangePasswordDialog = false, passwordChangedSuccess = false) }
    }

    fun changePassword(newPassword: String) {
        launchCatching(onError = { error -> _state.update { it.copy(error = error) } }) {
            changePasswordUseCase(newPassword)
                .onRight { _state.update { it.copy(showChangePasswordDialog = false, passwordChangedSuccess = true) } }
                .onLeft { error -> _state.update { it.copy(error = error) } }
        }
    }

    data class UIState(
        val userLogged: Boolean = false,
        val showLanguageDialog: Boolean = false,
        val showChangePasswordDialog: Boolean = false,
        val passwordChangedSuccess: Boolean = false,
        val error: ErrorBO? = null,
        val isLoading: Boolean = true,
    )
}
