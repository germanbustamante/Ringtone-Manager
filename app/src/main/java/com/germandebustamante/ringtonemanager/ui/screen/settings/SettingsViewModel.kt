package com.germandebustamante.ringtonemanager.ui.screen.settings

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.GetUserFlowUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignOutUserUseCase
import com.germandebustamante.ringtonemanager.ui.base.BaseViewModel
import com.germandebustamante.ringtonemanager.utils.extensions.collectEither

class SettingsViewModel(
    private val getUserFlowUseCase: GetUserFlowUseCase,
    private val signOutUserUseCase: SignOutUserUseCase,
    navigator: Navigator,
    context: Context,
) : BaseViewModel(navigator, context) {

    var state: UIState by mutableStateOf(UIState())
        private set

    init {
        launchCatching {
            getUserFlowUseCase().collectEither(
                onLeft = { state = state.copy(isLoading = false, error = it.toErrorString()) },
                onRight = { state = state.copy(userLogged = it != null, isLoading = false) }
            )
        }
    }

    fun signOut() {
        launchCatching {
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
        state = state.copy(error = null)
    }

    data class UIState(
        val userLogged: Boolean = false,
        val error: String? = null,
        val isLoading: Boolean = true,
    )
}