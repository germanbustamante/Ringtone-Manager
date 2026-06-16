package com.germandebustamante.ringtonemanager.ui.screenshot

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.germandebustamante.ringtonemanager.ui.screen.settings.SettingsContent
import com.germandebustamante.ringtonemanager.ui.screen.settings.SettingsViewModel
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun loggedIn() {
        paparazzi.snapshot {
            RingtoneManagerTheme(darkTheme = false, dynamicColor = false) {
                SettingsContent(
                    state = SettingsViewModel.UIState(userLogged = true, isLoading = false),
                    onSignInClicked = {},
                    onSignOutClicked = {},
                    onRegisterClicked = {},
                    onCleanError = {},
                    onChangeLanguageClicked = {},
                    onLanguageDialogDismissed = {},
                    onChangePasswordClicked = {},
                    onPasswordDialogDismissed = {},
                    onPasswordConfirmed = {},
                )
            }
        }
    }

    @Test
    fun loggedOut() {
        paparazzi.snapshot {
            RingtoneManagerTheme(darkTheme = false, dynamicColor = false) {
                SettingsContent(
                    state = SettingsViewModel.UIState(userLogged = false, isLoading = false),
                    onSignInClicked = {},
                    onSignOutClicked = {},
                    onRegisterClicked = {},
                    onCleanError = {},
                    onChangeLanguageClicked = {},
                    onLanguageDialogDismissed = {},
                    onChangePasswordClicked = {},
                    onPasswordDialogDismissed = {},
                    onPasswordConfirmed = {},
                )
            }
        }
    }
}
