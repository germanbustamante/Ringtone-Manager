package com.germandebustamante.ringtonemanager.ui.screenshot

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.ui.screen.login.LoginContent
import com.germandebustamante.ringtonemanager.ui.screen.login.LoginViewModel
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun default() {
        paparazzi.snapshot {
            RingtoneManagerTheme(darkTheme = false, dynamicColor = false) {
                LoginContent(
                    state = LoginViewModel.UIState(),
                    onEmailValueChanged = {},
                    onPasswordValueChanged = {},
                    onPasswordForgottenClicked = {},
                    onCreateNewAccountClicked = {},
                    onSignInButtonClicked = {},
                    onGoogleIdTokenReceived = {},
                    onBackPressed = {},
                    onCleanError = {},
                )
            }
        }
    }

    @Test
    fun loading() {
        paparazzi.snapshot {
            RingtoneManagerTheme(darkTheme = false, dynamicColor = false) {
                LoginContent(
                    state = LoginViewModel.UIState(loading = true),
                    onEmailValueChanged = {},
                    onPasswordValueChanged = {},
                    onPasswordForgottenClicked = {},
                    onCreateNewAccountClicked = {},
                    onSignInButtonClicked = {},
                    onGoogleIdTokenReceived = {},
                    onBackPressed = {},
                    onCleanError = {},
                )
            }
        }
    }

    @Test
    fun error() {
        paparazzi.snapshot {
            RingtoneManagerTheme(darkTheme = false, dynamicColor = false) {
                LoginContent(
                    state = LoginViewModel.UIState(error = ErrorBO.InvalidCredentials),
                    onEmailValueChanged = {},
                    onPasswordValueChanged = {},
                    onPasswordForgottenClicked = {},
                    onCreateNewAccountClicked = {},
                    onSignInButtonClicked = {},
                    onGoogleIdTokenReceived = {},
                    onBackPressed = {},
                    onCleanError = {},
                )
            }
        }
    }
}
