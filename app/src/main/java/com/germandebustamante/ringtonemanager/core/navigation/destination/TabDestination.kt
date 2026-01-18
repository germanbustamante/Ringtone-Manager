package com.germandebustamante.ringtonemanager.core.navigation.destination

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.germandebustamante.ringtonemanager.R
import kotlinx.serialization.Serializable

@Serializable
sealed interface TabDestination {

    @get:StringRes
    val name: Int

    @get:DrawableRes
    val selectedIcon: Int

    @get:DrawableRes
    val unselectedIcon: Int
    val defaultRoute: Destination

    @Serializable
    data object Home : TabDestination {
        override val name: Int = R.string.bottom_bar_home
        override val selectedIcon: Int = R.drawable.ic_home_filled
        override val unselectedIcon: Int = R.drawable.ic_home_outlined
        override val defaultRoute: Destination = Destination.HomeScreen
    }

    @Serializable
    data object Settings : TabDestination {
        override val name: Int = R.string.bottom_bar_settings
        override val selectedIcon: Int = R.drawable.ic_settings_filled
        override val unselectedIcon: Int = R.drawable.ic_settings_outlined
        override val defaultRoute: Destination = Destination.SettingsScreen
    }
}
