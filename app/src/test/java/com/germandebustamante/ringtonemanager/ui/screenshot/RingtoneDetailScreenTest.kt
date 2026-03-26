package com.germandebustamante.ringtonemanager.ui.screenshot

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBOMother
import com.germandebustamante.ringtonemanager.ui.screen.ringtone.RingtoneDetailContent
import com.germandebustamante.ringtonemanager.ui.screen.ringtone.RingtoneDetailViewModel
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme
import org.junit.Rule
import org.junit.Test

class RingtoneDetailScreenTest {

    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun loading() {
        paparazzi.snapshot {
            RingtoneManagerTheme(darkTheme = false, dynamicColor = false) {
                RingtoneDetailContent(
                    uiState = RingtoneDetailViewModel.RingtoneDetailUIState(isLoading = true),
                    onPlaybackPositionChange = {},
                    onPlayPauseButtonClick = {},
                    onSeekButtonClick = {},
                    onBackPressed = {},
                )
            }
        }
    }

    @Test
    fun loaded() {
        paparazzi.snapshot {
            RingtoneManagerTheme(darkTheme = false, dynamicColor = false) {
                RingtoneDetailContent(
                    uiState = RingtoneDetailViewModel.RingtoneDetailUIState(
                        isLoading = false,
                        ringtone = RingtoneBOMother.default(),
                        ringtoneDuration = 180_000,
                        isPlaying = false,
                    ),
                    onPlaybackPositionChange = {},
                    onPlayPauseButtonClick = {},
                    onSeekButtonClick = {},
                    onBackPressed = {},
                )
            }
        }
    }

    @Test
    fun playing() {
        paparazzi.snapshot {
            RingtoneManagerTheme(darkTheme = false, dynamicColor = false) {
                RingtoneDetailContent(
                    uiState = RingtoneDetailViewModel.RingtoneDetailUIState(
                        isLoading = false,
                        ringtone = RingtoneBOMother.default(),
                        ringtoneDuration = 180_000,
                        currentPlaybackPosition = 60_000,
                        isPlaying = true,
                    ),
                    onPlaybackPositionChange = {},
                    onPlayPauseButtonClick = {},
                    onSeekButtonClick = {},
                    onBackPressed = {},
                )
            }
        }
    }

    @Test
    fun error() {
        paparazzi.snapshot {
            RingtoneManagerTheme(darkTheme = false, dynamicColor = false) {
                RingtoneDetailContent(
                    uiState = RingtoneDetailViewModel.RingtoneDetailUIState(
                        isLoading = false,
                        error = ErrorBO.NotFound,
                    ),
                    onPlaybackPositionChange = {},
                    onPlayPauseButtonClick = {},
                    onSeekButtonClick = {},
                    onBackPressed = {},
                )
            }
        }
    }
}
