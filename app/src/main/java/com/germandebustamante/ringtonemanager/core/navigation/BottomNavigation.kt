package com.germandebustamante.ringtonemanager.core.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.germandebustamante.ringtonemanager.core.navigation.bottom.screen.BottomScreens

@Composable
fun AppBottomNavigation(navController: NavController) {
    val topLevelRoutes = listOf(
        BottomScreens.Home,
        BottomScreens.MyAccount,
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        topLevelRoutes.forEach { topLevelRoute ->
            val name = stringResource(topLevelRoute.name)
            NavigationBarItem(
                icon = { Icon(painterResource(topLevelRoute.icon), contentDescription = name) },
                label = { Text(name) },
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