package com.germandebustamante.ringtonemanager.ui.screenshot

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBOMother
import com.germandebustamante.ringtonemanager.ui.screen.home.HomeContent
import com.germandebustamante.ringtonemanager.ui.screen.home.HomeViewModel
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun loading() {
        paparazzi.snapshot {
            RingtoneManagerTheme(darkTheme = false, dynamicColor = false) {
                HomeContent(
                    state = HomeViewModel.UIState(isLoading = true),
                    onRingtoneClicked = {},
                    onPlayRingtoneClicked = { _, _ -> },
                    onLoadMoreClicked = {},
                )
            }
        }
    }

    @Test
    fun content() {
        paparazzi.snapshot {
            RingtoneManagerTheme(darkTheme = false, dynamicColor = false) {
                HomeContent(
                    state = HomeViewModel.UIState(
                        isLoading = false,
                        ringtones = RingtoneBOMother.randomList(5),
                    ),
                    onRingtoneClicked = {},
                    onPlayRingtoneClicked = { _, _ -> },
                    onLoadMoreClicked = {},
                )
            }
        }
    }

    @Test
    fun playing() {
        val ringtones = RingtoneBOMother.randomList(3)
        paparazzi.snapshot {
            RingtoneManagerTheme(darkTheme = false, dynamicColor = false) {
                HomeContent(
                    state = HomeViewModel.UIState(
                        isLoading = false,
                        ringtones = ringtones,
                        currentRingtonePlayingId = ringtones.first().id,
                    ),
                    onRingtoneClicked = {},
                    onPlayRingtoneClicked = { _, _ -> },
                    onLoadMoreClicked = {},
                )
            }
        }
    }

    @Test
    fun error() {
        paparazzi.snapshot {
            RingtoneManagerTheme(darkTheme = false, dynamicColor = false) {
                HomeContent(
                    state = HomeViewModel.UIState(
                        isLoading = false,
                        error = ErrorBO.Unknown("Network error"),
                    ),
                    onRingtoneClicked = {},
                    onPlayRingtoneClicked = { _, _ -> },
                    onLoadMoreClicked = {},
                )
            }
        }
    }
}
