package com.germandebustamante.ringtonemanager.ui.screen.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.GetUserFlowUseCase
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignOutUserUseCase
import com.germandebustamante.ringtonemanager.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getUserFlowUseCase: GetUserFlowUseCase,
    private val signOutUserUseCase: SignOutUserUseCase
) : BaseViewModel() {


    var state by mutableStateOf(UIState())
        private set

    init {
        viewModelScope.launch {
            getUserFlowUseCase().collect {
                state = state.copy(userLogged = it != null)
            }
        }

    }

    fun signOut() {
        viewModelScope.launch {
            signOutUserUseCase()
        }
    }

    data class UIState(
        val userLogged: Boolean = false,
    )
}