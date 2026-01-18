package com.germandebustamante.ringtonemanager.core.navigation.action

import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination

sealed interface NavigationAction {
    data class Navigate(val destination: Destination) : NavigationAction
    data object Back : NavigationAction
}
