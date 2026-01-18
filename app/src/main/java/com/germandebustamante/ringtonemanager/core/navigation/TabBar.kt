package com.germandebustamante.ringtonemanager.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.core.navigation.destination.TabDestination

private val tabDestinations = listOf(
    TabDestination.Home,
    TabDestination.Settings
)

/**
 * Bottom navigation bar with tab selection.
 *
 * @param selectedTab Currently selected tab
 * @param onTabSelected Callback invoked when a tab is selected
 */
@Composable
fun TabBar(
    selectedTab: TabDestination,
    onTabSelected: (TabDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        tabDestinations.forEach { tab ->
            val isSelected = selectedTab == tab

            NavigationBarItem(
                icon = { TabIcon(tab, isSelected) },
                label = { TabLabel(tab, isSelected) },
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
private fun TabLabel(tab: TabDestination, isSelected: Boolean) {
    val labelText = stringResource(tab.name)
    TextWithUnderline(text = labelText, showUnderline = isSelected)
}

@Composable
private fun TabIcon(tab: TabDestination, isSelected: Boolean) {
    val iconRes = if (isSelected) tab.selectedIcon else tab.unselectedIcon
    val contentDescription = stringResource(tab.name)

    Icon(
        painter = painterResource(iconRes),
        contentDescription = contentDescription
    )
}

@Composable
private fun TextWithUnderline(text: String, showUnderline: Boolean) {
    val underlineColor = MaterialTheme.colorScheme.primary
    val modifier = if (showUnderline) {
        Modifier
            .drawBehind {
                val verticalOffset = size.height + 2.dp.toPx()
                drawLine(
                    color = underlineColor,
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
        modifier = modifier
    )
}
