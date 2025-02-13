package com.germandebustamante.ringtonemanager.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.germandebustamante.ringtonemanager.core.navigation.bottom.screen.HomeScreen
import com.germandebustamante.ringtonemanager.core.navigation.bottom.screen.LoginScreen
import com.germandebustamante.ringtonemanager.core.navigation.bottom.screen.RegisterScreen
import com.germandebustamante.ringtonemanager.core.navigation.bottom.screen.RingtoneDetailScreen
import com.germandebustamante.ringtonemanager.core.navigation.bottom.screen.SettingsScreen
import com.germandebustamante.ringtonemanager.ui.screen.home.HomeScreen
import com.germandebustamante.ringtonemanager.ui.screen.login.LoginScreen
import com.germandebustamante.ringtonemanager.ui.screen.register.RegisterScreen
import com.germandebustamante.ringtonemanager.ui.screen.ringtone.RingtoneDetailScreen
import com.germandebustamante.ringtonemanager.ui.screen.settings.SettingsScreen

@Composable
fun NavigationWrapper(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = HomeScreen, modifier = modifier) {
        composable<HomeScreen>(enterTransition = null, exitTransition = null) {
            HomeScreen(onRingtoneClicked = {
                navController.navigate(RingtoneDetailScreen(it))
            })
        }

        composable<SettingsScreen>(enterTransition = null, exitTransition = null) {
            SettingsScreen(
                onSignInClicked = { navController.navigate(LoginScreen) },
                onRegisterClicked = { navController.navigate(RegisterScreen) },
            )
        }

        composable<RingtoneDetailScreen>(enterTransition = null, exitTransition = null) {
            RingtoneDetailScreen(onBackPressed = {
                navController.popBackStack()
            })
        }

        composable<LoginScreen> {
            LoginScreen(onUserLogged = {
                navController.popBackStack()
            }, onBackPressed = {
                navController.popBackStack()
            })
        }

        composable<RegisterScreen> {
            RegisterScreen(onBackPressed = {
                navController.popBackStack()
            })
        }
    }
}