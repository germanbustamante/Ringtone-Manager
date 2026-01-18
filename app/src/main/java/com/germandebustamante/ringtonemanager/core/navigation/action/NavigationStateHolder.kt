package com.germandebustamante.ringtonemanager.core.navigation.action

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.core.navigation.destination.TabDestination

/**
 * Creates and remembers a navigation state holder that persists across config changes
 * and process death.
 *
 * @param startTab The initial tab to display. Defaults to Home.
 * @return A NavigationStateHolder instance.
 */
@Composable
fun rememberNavigationState(
    startTab: TabDestination = TabDestination.Home,
): NavigationStateHolder {
    // Create a NavBackStack for each tab using Navigation 3's rememberNavBackStack
    val homeBackStack = rememberNavBackStack(TabDestination.Home.defaultRoute)
    val settingsBackStack = rememberNavBackStack(TabDestination.Settings.defaultRoute)

    @Suppress("UNCHECKED_CAST")
    val tabsBackStack = remember(homeBackStack, settingsBackStack) {
        mapOf(
            TabDestination.Home to homeBackStack as NavBackStack<Destination>,
            TabDestination.Settings to settingsBackStack as NavBackStack<Destination>
        )
    }

    val selectedTab = remember { mutableStateOf(startTab) }

    return remember(startTab, tabsBackStack) {
        NavigationStateHolder(
            startTab = startTab,
            selectedTab = selectedTab,
            backStacks = tabsBackStack
        )
    }
}

/**
 * State holder for multi-tab navigation with separate back stacks per tab.
 *
 * This class manages navigation state using Navigation 3's NavBackStack for each tab,
 * ensuring proper state preservation across configuration changes and process death.
 *
 * @param startTab The starting tab (used for "exit through home" pattern)
 * @param selectedTab Mutable state tracking the currently selected tab
 * @param backStacks Map of tab destinations to their respective NavBackStacks
 */
@Stable
class NavigationStateHolder(
    val startTab: TabDestination,
    private val selectedTab: MutableState<TabDestination>,
    val backStacks: Map<TabDestination, NavBackStack<Destination>>,
) {

    var currentTab: TabDestination by selectedTab

    /**
     * Navigates to a destination. Distinguishes between tab-level navigation
     * and in-tab navigation.
     *
     * @param destination The destination to navigate to
     */
    fun navigate(destination: Destination) {
        // Check if destination is a tab's default route
        val targetTab = findTabForDestination(destination)

        if (targetTab != null) {
            // This is a tab-level navigation, switch tabs
            currentTab = targetTab
        } else {
            // This is in-tab navigation, add to current tab's back stack
            val currentStack = backStacks[currentTab]
            currentStack?.add(destination)
        }
    }

    /**
     * Handles back navigation. Pops from current tab's stack or switches
     * to start tab using "exit through home" pattern.
     *
     * @return true if back was handled, false if should exit app
     */
    fun navigateBack(): Boolean {
        val currentStack = backStacks[currentTab]
        val currentDestination = currentStack?.lastOrNull()

        // If at tab root and not on start tab, switch to start tab
        if (currentDestination == currentTab.defaultRoute && currentTab != startTab) {
            currentTab = startTab
            return true
        }

        // If at tab root and on start tab, exit app
        if (currentDestination == currentTab.defaultRoute && currentTab == startTab) {
            return false
        }

        // Pop from current stack
        currentStack?.removeLastOrNull()
        return true
    }

    /**
     * Handles tab selection. Switches to the selected tab.
     *
     * @param tab The tab to switch to
     */
    fun onTabSelected(tab: TabDestination) {
        currentTab = tab
    }

    /**
     * Converts the navigation state into decorated NavEntries for NavDisplay.
     *
     * Each tab's back stack gets its own SaveableStateHolder decorator to preserve
     * state independently. Also applies ViewModel scoping across all entries.
     *
     * @param entryProvider Function that creates NavEntry for a given Destination
     * @return List of decorated entries for currently active tabs
     */
    @Composable
    fun toDecoratedEntries(
        entryProvider: (Destination) -> NavEntry<Destination>,
    ): SnapshotStateList<NavEntry<Destination>> {
        // Create decorated entries for each back stack with its own SaveableStateHolder
        val decoratedEntries = backStacks.mapValues { (_, stack) ->
            val decorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator<Destination>()
            )
            rememberDecoratedNavEntries(
                backStack = stack,
                entryDecorators = decorators,
                entryProvider = entryProvider
            )
        }

        // Return entries for tabs currently in use (exit through home pattern)
        return getActiveTabRoutes()
            .flatMap { tab -> decoratedEntries[tab] ?: emptyList() }
            .toMutableStateList()
    }

    /**
     * Gets the tabs that should have their entries displayed.
     * Implements "exit through home" pattern: start tab is always active,
     * plus current tab if different.
     */
    private fun getActiveTabRoutes(): List<TabDestination> = if (currentTab == startTab) {
        listOf(startTab)
    } else {
        listOf(startTab, currentTab)
    }

    /**
     * Finds which tab a destination belongs to by checking if it's a tab's default route.
     */
    private fun findTabForDestination(destination: Destination): TabDestination? = backStacks.keys.firstOrNull { tab ->
        tab.defaultRoute == destination
    }
}