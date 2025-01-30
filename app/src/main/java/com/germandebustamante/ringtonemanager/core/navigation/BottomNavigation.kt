package com.germandebustamante.ringtonemanager.core.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.germandebustamante.ringtonemanager.core.navigation.bottom.screen.BottomScreens
import com.germandebustamante.ringtonemanager.ui.theme.color_text_300
import com.germandebustamante.ringtonemanager.ui.theme.color_text_500

@Composable
fun AppBottomNavigation(navController: NavController) {
    val topLevelRoutes = listOf(
        BottomScreens.Home,
        BottomScreens.MyAccount,
    )

    NavigationBar(
        containerColor = color_text_500,
        windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp, left = 0.dp, right = 0.dp),
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        topLevelRoutes.forEach { topLevelRoute ->
            val name = stringResource(topLevelRoute.name)
            val isSelected = currentDestination?.hierarchy?.any { it.route == topLevelRoute.route::class.qualifiedName } == true
            NavigationBarItem(
                icon = { Icon(painterResource(topLevelRoute.icon), contentDescription = name, tint = getSelectedColor(isSelected)) },
                label = { Text(name, color = getSelectedColor(isSelected)) },
                selected = currentDestination?.hierarchy?.any { it.route == topLevelRoute.route::class.qualifiedName } == true,
                onClick = {
                    navController.navigate(topLevelRoute.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
private fun getSelectedColor(isSelected: Boolean): Color = if (isSelected) Color.White else color_text_300