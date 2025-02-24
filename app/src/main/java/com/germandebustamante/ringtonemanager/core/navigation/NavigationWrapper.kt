package com.germandebustamante.ringtonemanager.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.ui.screen.forgotpassword.ForgotPasswordScreen
import com.germandebustamante.ringtonemanager.ui.screen.home.HomeScreen
import com.germandebustamante.ringtonemanager.ui.screen.login.LoginScreen
import com.germandebustamante.ringtonemanager.ui.screen.register.RegisterScreen
import com.germandebustamante.ringtonemanager.ui.screen.ringtone.RingtoneDetailScreen
import com.germandebustamante.ringtonemanager.ui.screen.settings.SettingsScreen

@Composable
fun NavigationWrapper(navController: NavHostController, navigator: Navigator, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = navigator.startDestination, modifier = modifier) {
        composable<Destination.HomeScreen> {
            HomeScreen()
        }

        composable<Destination.SettingsScreen> {
            SettingsScreen()
        }

        composable<Destination.RingtoneDetailScreen>(enterTransition = null, exitTransition = null) {
            RingtoneDetailScreen()
        }

        composable<Destination.LoginScreen> {
            LoginScreen()
        }

        composable<Destination.RegisterScreen> {
            RegisterScreen()
        }

        composable<Destination.ForgotPasswordScreen> {
            ForgotPasswordScreen()
        }
    }
}