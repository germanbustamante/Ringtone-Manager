package com.germandebustamante.ringtonemanager.core.navigation.bottom.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.germandebustamante.ringtonemanager.R
import kotlinx.serialization.Serializable

@Serializable
sealed class BottomScreens<T>(@StringRes val name: Int,@DrawableRes val icon: Int, val route: T) {
    @Serializable
    data object Home : BottomScreens<HomeScreen>(
        name = R.string.bottom_bar_home,
        icon = R.drawable.ic_home,
        route = HomeScreen
    )

    @Serializable
    data object MyAccount : BottomScreens<MyAccountScreen>(
        name = R.string.bottom_bar_my_account,
        icon = R.drawable.ic_my_account,
        route = MyAccountScreen
    )
}