package com.germandebustamante.ringtonemanager.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.germandebustamante.ringtonemanager.core.navigation.bottom.screen.HomeScreen
import com.germandebustamante.ringtonemanager.core.navigation.bottom.screen.MyAccountScreen
import com.germandebustamante.ringtonemanager.core.navigation.bottom.screen.RingtoneDetailScreen
import com.germandebustamante.ringtonemanager.ui.screen.home.HomeScreen
import com.germandebustamante.ringtonemanager.ui.screen.myaccount.MyAccountScreen
import com.germandebustamante.ringtonemanager.ui.screen.ringtone.RingtoneDetailScreen

@Composable
fun NavigationWrapper(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = HomeScreen, modifier = modifier) {
        composable<HomeScreen>(enterTransition = null, exitTransition = null) {
            HomeScreen(onRingtoneClicked = {
                navController.navigate(RingtoneDetailScreen(it))
            })
        }

        composable<MyAccountScreen>(enterTransition = null, exitTransition = null) {
            MyAccountScreen()
        }

        composable<RingtoneDetailScreen>(enterTransition = null, exitTransition = null) {
            RingtoneDetailScreen(navController)
        }
    }
}