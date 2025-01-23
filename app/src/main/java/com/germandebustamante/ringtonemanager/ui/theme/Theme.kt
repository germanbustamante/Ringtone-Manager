package com.germandebustamante.ringtonemanager.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Color.White,
    onPrimary = color_brand_primary_700,
    //primaryContainer = ,
    //onPrimaryContainer =,
    //inversePrimary = ,
    //secondary =,
    onSecondary = color_background_5,
    //secondaryContainer =,
    //onSecondaryContainer =,
    //tertiary =,
    //onTertiary =,
    //tertiaryContainer =,
    //onTertiaryContainer =,
    //background =,
    onBackground = color_background_1,
    surface = color_brand_primary_light_500,
    onSurface = color_brand_primary_light_500,
    //surfaceVariant =,
    //onSurfaceVariant =,
    //surfaceTint =,
    //inverseSurface =,
    //inverseOnSurface =,
    //error =,
    onError = color_danger_700,
    //errorContainer =,
    //onErrorContainer =,
    //outline =,
    //outlineVariant =,
    //scrim = ,
)

@Composable
fun RingtoneManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}