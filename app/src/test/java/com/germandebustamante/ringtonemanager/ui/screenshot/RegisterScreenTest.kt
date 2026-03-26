package com.germandebustamante.ringtonemanager.ui.screenshot

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.ui.screen.register.RegisterContent
import com.germandebustamante.ringtonemanager.ui.screen.register.RegisterViewModel
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme
import org.junit.Rule
import org.junit.Test

class RegisterScreenTest {

    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun default() {
        paparazzi.snapshot {
            RingtoneManagerTheme(darkTheme = false, dynamicColor = false) {
                RegisterContent(
                    state = RegisterViewModel.UIState(),
                    onEmailValueChanged = {},
                    onNameValueChanged = {},
                    onPasswordValueChanged = {},
                    onCurrentPasswordValueChanged = {},
                    onRegisterButtonClicked = {},
                    onGoToLoginButtonClicked = {},
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
                RegisterContent(
                    state = RegisterViewModel.UIState(error = ErrorBO.EmailAddressAlreadyInUse),
                    onEmailValueChanged = {},
                    onNameValueChanged = {},
                    onPasswordValueChanged = {},
                    onCurrentPasswordValueChanged = {},
                    onRegisterButtonClicked = {},
                    onGoToLoginButtonClicked = {},
                    onGoogleIdTokenReceived = {},
                    onBackPressed = {},
                    onCleanError = {},
                )
            }
        }
    }
}
