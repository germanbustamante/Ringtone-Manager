package com.germandebustamante.ringtonemanager.core.navigation.bottom.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.germandebustamante.ringtonemanager.R
import kotlinx.serialization.Serializable

@Serializable
sealed class BottomScreens<T>(
    @StringRes val name: Int,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    val route: T,
) {
    @Serializable
    data object Home : BottomScreens<HomeScreen>(
        name = R.string.bottom_bar_home,
        selectedIcon = R.drawable.ic_home_filled,
        unselectedIcon = R.drawable.ic_home_outlined,
        route = HomeScreen
    )

    @Serializable
    data object MyAccount : BottomScreens<MyAccountScreen>(
        name = R.string.bottom_bar_my_account,
        selectedIcon = R.drawable.ic_my_account_filled,
        unselectedIcon = R.drawable.ic_my_account_outlined,
        route = MyAccountScreen
    )
}