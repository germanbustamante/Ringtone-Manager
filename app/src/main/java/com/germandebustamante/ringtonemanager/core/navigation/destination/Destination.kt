package com.germandebustamante.ringtonemanager.core.navigation.destination

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    data object HomeScreen : Destination

    @Serializable
    data object SettingsScreen : Destination

    @Serializable
    data class RingtoneDetailScreen(val ringtoneId: String) : Destination

    @Serializable
    data object LoginScreen : Destination

    @Serializable
    data object RegisterScreen : Destination
}