package com.germandebustamante.ringtonemanager.core.navigation.destination

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.germandebustamante.ringtonemanager.R
import kotlinx.serialization.Serializable

@Serializable
sealed class BottomScreenDestination<T>(
    @StringRes val name: Int,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    val route: T,
) {
    @Serializable
    data object Home : BottomScreenDestination<Destination.HomeScreen>(
        name = R.string.bottom_bar_home,
        selectedIcon = R.drawable.ic_home_filled,
        unselectedIcon = R.drawable.ic_home_outlined,
        route = Destination.HomeScreen
    )

    @Serializable
    data object Settings : BottomScreenDestination<Destination.SettingsScreen>(
        name = R.string.bottom_bar_settings,
        selectedIcon = R.drawable.ic_settings_filled,
        unselectedIcon = R.drawable.ic_settings_outlined,
        route = Destination.SettingsScreen
    )
}