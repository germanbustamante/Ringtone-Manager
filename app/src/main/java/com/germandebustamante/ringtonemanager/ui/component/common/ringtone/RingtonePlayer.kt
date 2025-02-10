package com.germandebustamante.ringtonemanager.ui.component.common.ringtone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.utils.audio.AudioUtils

/**
 * A composable for controlling and displaying a ringtone player's state.
 *
 * @param currentPosition The current playback position in milliseconds.
 * @param duration The total duration of the audio in milliseconds.
 * @param isPlaying Whether the audio is currently playing.
 * @param onPlaybackPositionChange Callback when the playback position is updated.
 * @param onPlayPauseButtonClick Callback when the play/pause button is clicked.
 * @param modifier Modifier for applying additional styling.
 */
@Composable
fun RingtonePlayer(
    currentPosition: Int,
    duration: Int,
    isPlaying: Boolean,
    onPlaybackPositionChange: (Int) -> Unit,
    onPlayPauseButtonClick: () -> Unit,
    onSeekButtonClick: (timeInMillis: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        RingtoneSlider(
            currentPosition = currentPosition,
            duration = duration,
            onPlaybackPositionChange = onPlaybackPositionChange,
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = AudioUtils.getFormattedAudioDuration(currentPosition),
                style = MaterialTheme.typography.bodySmall,
            )

            Text(
                text = AudioUtils.getFormattedAudioDuration(duration),
                style = MaterialTheme.typography.bodySmall
            )
        }

        val buttonSize = IconPrimaryButtonSize.EXTRA_LARGE
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = buttonSize.dp * PLAY_PAUSE_BTN_SIZE_FACTOR + 4.dp)
                .padding(top = 16.dp),
        ) {

            SeekButton(
                onClick = onSeekButtonClick,
                action = TimeNavigationAction.BACKWARD_TEN_SECONDS,
                enabled = isPlaying
            )

            PlayPauseRingtoneButton(
                isPlaying = isPlaying,
                onPlayPauseButtonClick = onPlayPauseButtonClick,
                size = buttonSize,
            )

            SeekButton(
                onClick = onSeekButtonClick,
                action = TimeNavigationAction.FORWARD_TEN_SECONDS,
                enabled = isPlaying
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun RingtonePlayerPreview() {
    var currentPosition by remember { mutableIntStateOf(0) }
    val duration = 8976

    RingtonePlayer(
        currentPosition = currentPosition,
        onPlaybackPositionChange = { currentPosition = it },
        duration = duration,
        onPlayPauseButtonClick = {},
        onSeekButtonClick = {},
        isPlaying = false,
        modifier = Modifier.padding(16.dp),
    )
}
