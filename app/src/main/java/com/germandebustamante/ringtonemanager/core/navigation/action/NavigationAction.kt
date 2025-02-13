package com.germandebustamante.ringtonemanager.core.navigation.action

import androidx.navigation.NavOptionsBuilder
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination

sealed interface NavigationAction {

    data class Navigate(
        val destination: Destination,
        val navOptions: NavOptionsBuilder.() -> Unit = {},
    ): NavigationAction

    data object NavigateUp: NavigationAction
}