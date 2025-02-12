package com.germandebustamante.ringtonemanager.core.navigation.bottom.screen

import kotlinx.serialization.Serializable

@Serializable
object HomeScreen

@Serializable
object SettingsScreen

@Serializable
class RingtoneDetailScreen(val ringtoneId: String)

@Serializable
object LoginScreen

@Serializable
object RegisterScreen