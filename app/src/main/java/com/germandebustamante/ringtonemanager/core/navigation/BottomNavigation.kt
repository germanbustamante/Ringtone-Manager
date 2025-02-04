package com.germandebustamante.ringtonemanager.core.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.germandebustamante.ringtonemanager.core.navigation.bottom.screen.BottomScreens

@Composable
fun AppBottomNavigation(navController: NavController) {
    val topLevelRoutes: List<BottomScreens<out Any>> = listOf(BottomScreens.Home, BottomScreens.MyAccount)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isTopLevelRoute = isCurrentDestinationTopLevel(currentDestination, topLevelRoutes)

    if (isTopLevelRoute) {
        BottomNavigationBar(navController, topLevelRoutes, currentDestination)
    }
}

@Composable
private fun BottomNavigationBar(
    navController: NavController,
    topLevelRoutes: List<BottomScreens<out Any>>,
    currentDestination: NavDestination?,
) {
    val lineColor = MaterialTheme.colorScheme.onBackground

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        windowInsets = WindowInsets(bottom = 30.dp),
        modifier = Modifier
            .drawBehind {
                drawLine(
                    color = lineColor,
                    strokeWidth = 1.dp.toPx(),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                )
            }
            .heightIn(max = 110.dp)
    ) {
        topLevelRoutes.forEach { route ->
            NavigationBarItem(
                icon = { NavigationIcon(route, currentDestination) },
                label = { NavigationLabel(route, currentDestination) },
                selected = false, // False to avoid the default ripple effect
                onClick = { navigateToRoute(navController, route) }
            )
        }
    }
}

@Composable
private fun NavigationLabel(route: BottomScreens<out Any>, currentDestination: NavDestination?) {
    val isSelected = isRouteSelected(route, currentDestination)
    val labelText = stringResource(route.name)

    TextWithUnderline(text = labelText, underline = isSelected)
}

@Composable
private fun NavigationIcon(route: BottomScreens<out Any>, currentDestination: NavDestination?) {
    val isSelected = isRouteSelected(route, currentDestination)
    val iconRes = if (isSelected) route.selectedIcon else route.unselectedIcon
    val contentDescription = stringResource(route.name)

    Icon(
        painter = painterResource(iconRes),
        contentDescription = contentDescription,
        tint = getSelectedColor(isSelected)
    )
}

@Composable
private fun TextWithUnderline(text: String, underline: Boolean) {
    val color = getSelectedColor(underline)
    val modifier = if (underline) {
        Modifier
            .drawBehind {
                val verticalOffset = size.height + 2.dp.toPx()
                drawLine(
                    color = color,
                    strokeWidth = 2.dp.toPx(),
                    start = Offset(0f, verticalOffset),
                    end = Offset(size.width, verticalOffset),
                )
            }
            .padding(horizontal = 4.dp)
    } else {
        Modifier
    }

    Text(
        text = text,
        color = color,
        modifier = modifier
    )
}

@Composable
private fun getSelectedColor(isSelected: Boolean): Color =
    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

private fun isCurrentDestinationTopLevel(
    currentDestination: NavDestination?,
    topLevelRoutes: List<BottomScreens<out Any>>,
): Boolean {
    return topLevelRoutes.any { it.route::class.qualifiedName == currentDestination?.route }
}

private fun isRouteSelected(
    route: BottomScreens<out Any>,
    currentDestination: NavDestination?,
): Boolean = currentDestination?.hierarchy?.any { it.route == route.route::class.qualifiedName } == true

private fun navigateToRoute(navController: NavController, route: BottomScreens<out Any>) {
    navController.navigate(route.route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = false
        }
        launchSingleTop = false
        restoreState = true
    }
}