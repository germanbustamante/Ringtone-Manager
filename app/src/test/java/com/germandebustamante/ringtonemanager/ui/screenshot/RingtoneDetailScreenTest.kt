package com.germandebustamante.ringtonemanager.ui.screenshot

import androidx.compose.material3.SnackbarHostState
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

    private val snackbarHostState = SnackbarHostState()

    @Test
    fun loading() {
        paparazzi.snapshot {
            RingtoneManagerTheme(darkTheme = false, dynamicColor = false) {
                RingtoneDetailContent(
                    uiState = RingtoneDetailViewModel.RingtoneDetailUIState(isLoading = true),
                    snackbarHostState = snackbarHostState,
                    onPlaybackPositionChange = {},
                    onPlayPauseButtonClick = {},
                    onSeekButtonClick = {},
                    onBackPressed = {},
                    onToggleFavoriteClicked = {},
                    onShareClicked = {},
                    onSetAsRingtoneClicked = {},
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
                    snackbarHostState = snackbarHostState,
                    onPlaybackPositionChange = {},
                    onPlayPauseButtonClick = {},
                    onSeekButtonClick = {},
                    onBackPressed = {},
                    onToggleFavoriteClicked = {},
                    onShareClicked = {},
                    onSetAsRingtoneClicked = {},
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
                    snackbarHostState = snackbarHostState,
                    onPlaybackPositionChange = {},
                    onPlayPauseButtonClick = {},
                    onSeekButtonClick = {},
                    onBackPressed = {},
                    onToggleFavoriteClicked = {},
                    onShareClicked = {},
                    onSetAsRingtoneClicked = {},
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
                    snackbarHostState = snackbarHostState,
                    onPlaybackPositionChange = {},
                    onPlayPauseButtonClick = {},
                    onSeekButtonClick = {},
                    onBackPressed = {},
                    onToggleFavoriteClicked = {},
                    onShareClicked = {},
                    onSetAsRingtoneClicked = {},
                )
            }
        }
    }
}
