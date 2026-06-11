package com.germandebustamante.ringtonemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import com.germandebustamante.ringtonemanager.core.navigation.NavigationHost
import com.germandebustamante.ringtonemanager.core.navigation.ObserveAsEvent
import com.germandebustamante.ringtonemanager.core.navigation.TabBar
import com.germandebustamante.ringtonemanager.core.navigation.action.NavigationAction
import com.germandebustamante.ringtonemanager.core.navigation.action.rememberNavigationState
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.ui.screen.forgotpassword.ForgotPasswordScreen
import com.germandebustamante.ringtonemanager.ui.screen.home.HomeScreen
import com.germandebustamante.ringtonemanager.ui.screen.login.LoginScreen
import com.germandebustamante.ringtonemanager.ui.screen.register.RegisterScreen
import com.germandebustamante.ringtonemanager.ui.screen.ringtone.RingtoneDetailScreen
import com.germandebustamante.ringtonemanager.ui.screen.settings.SettingsScreen
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent()
    }

    private fun setContent() {
        setContent {
            val navigationState = rememberNavigationState()
            val navigator = koinInject<Navigator>()

            // Observe navigation actions from ViewModels (via Navigator interface)
            // and update the navigation state accordingly
            ObserveAsEvent(flow = navigator.navigationActions) { action ->
                when (action) {
                    is NavigationAction.Navigate -> navigationState.navigate(action.destination)
                    is NavigationAction.Back -> navigationState.navigateBack()
                }
            }

            RingtoneManagerTheme {
                Scaffold(
                    bottomBar = {
                        TabBar(
                            selectedTab = navigationState.currentTab,
                            onTabSelected = navigationState::onTabSelected
                        )
                    }
                ) { padding ->
                    // Convert navigation state to decorated entries and display
                    val decoratedEntries = navigationState.toDecoratedEntries(navigationEntries())

                    NavigationHost(
                        entries = decoratedEntries,
                        onBack = { navigationState.navigateBack() },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }

    @Composable
    private fun navigationEntries(): (Destination) -> NavEntry<Destination> = entryProvider {
        entry<Destination.HomeScreen> {
            HomeScreen()
        }
        entry<Destination.SettingsScreen> {
            SettingsScreen()
        }
        entry<Destination.RingtoneDetailScreen> { ringtoneDetail ->
            RingtoneDetailScreen(ringtoneDetail)
        }
        entry<Destination.LoginScreen> {
            LoginScreen()
        }
        entry<Destination.RegisterScreen> {
            RegisterScreen()
        }
        entry<Destination.ForgotPasswordScreen> {
            ForgotPasswordScreen()
        }
    }
}