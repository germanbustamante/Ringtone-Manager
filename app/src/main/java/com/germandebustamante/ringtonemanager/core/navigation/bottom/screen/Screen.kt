package com.germandebustamante.ringtonemanager.core.navigation.bottom.screen

import kotlinx.serialization.Serializable

@Serializable
object HomeScreen

@Serializable
object MyAccountScreen

@Serializable
class RingtoneDetailScreen(val ringtoneId: String)