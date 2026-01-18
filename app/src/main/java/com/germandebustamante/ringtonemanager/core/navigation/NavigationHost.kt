package com.germandebustamante.ringtonemanager.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination

/**
 * Main navigation host that displays the current destination based on decorated entries.
 *
 * This composable uses Navigation 3's NavDisplay to handle navigation transitions.
 * The entries are already decorated by NavigationStateHolder.toDecoratedEntries() with
 * SaveableStateHolder (per tab) and ViewModel scoping.
 *
 * @param entries The decorated navigation entries to display
 * @param onBack Callback invoked when the user navigates back
 * @param modifier Optional modifier for the navigation host
 */
@Composable
fun NavigationHost(
    entries: SnapshotStateList<NavEntry<Destination>>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavDisplay(
        entries = entries,
        onBack = onBack,
        modifier = modifier
    )
}
