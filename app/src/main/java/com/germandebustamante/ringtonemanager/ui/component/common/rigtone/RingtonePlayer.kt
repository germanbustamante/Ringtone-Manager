package com.germandebustamante.ringtonemanager.ui.component.common.rigtone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                .fillMaxWidth()
                .padding(top = 8.dp),
        ) {
            Text(text = AudioUtils.getFormattedAudioDuration(currentPosition))
            Text(text = AudioUtils.getFormattedAudioDuration(duration))
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            PlayPauseRingtoneButton(
                isPlaying = isPlaying,
                onPlayPauseButtonClick = onPlayPauseButtonClick,
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
        isPlaying = false,
        modifier = Modifier.padding(16.dp),
    )
}
