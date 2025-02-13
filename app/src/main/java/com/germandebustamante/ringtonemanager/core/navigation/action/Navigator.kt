package com.germandebustamante.ringtonemanager.core.navigation.action

import androidx.navigation.NavOptionsBuilder
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow

interface Navigator {
    val startDestination: Destination
    val navigationActions: Flow<NavigationAction>

    suspend fun navigate(
        destination: Destination,
        navOptions: NavOptionsBuilder.() -> Unit = {},
    )

    suspend fun navigateUp()
}

class DefaultNavigator(
    override val startDestination: Destination,
) : Navigator {

    private val _navigationActions = Channel<NavigationAction>()
    override val navigationActions: Flow<NavigationAction> = _navigationActions.consumeAsFlow()

    override suspend fun navigate(destination: Destination, navOptions: NavOptionsBuilder.() -> Unit) {
        _navigationActions.send(
            NavigationAction.Navigate(
                destination = destination,
                navOptions = navOptions
            )
        )
    }

    override suspend fun navigateUp() {
        _navigationActions.send(NavigationAction.NavigateUp)
    }
}