package com.germandebustamante.ringtonemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.germandebustamante.ringtonemanager.core.navigation.AppBottomNavigation
import com.germandebustamante.ringtonemanager.core.navigation.NavigationWrapper
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            RingtoneManagerTheme {
                Scaffold(bottomBar = {
                    AppBottomNavigation(navController)
                }, modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationWrapper(navController, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}