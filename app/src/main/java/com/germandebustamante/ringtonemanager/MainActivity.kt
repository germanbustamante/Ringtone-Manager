package com.germandebustamante.ringtonemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.germandebustamante.ringtonemanager.core.navigation.AppBottomNavigation
import com.germandebustamante.ringtonemanager.core.navigation.NavigationWrapper
import com.germandebustamante.ringtonemanager.ui.screen.splash.SplashScreen
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.initialize

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.initialize(this)
        Firebase.appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance(),
        )

        enableEdgeToEdge()
        setContent {
            var showSplashScreen by rememberSaveable { mutableStateOf(true) }
            val navController = rememberNavController()

            RingtoneManagerTheme {
                if (showSplashScreen) {
                    SplashScreen(onSplashFinished = { showSplashScreen = false })
                } else {
                    Scaffold(
                        bottomBar = { AppBottomNavigation(navController) },
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        NavigationWrapper(navController, modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}