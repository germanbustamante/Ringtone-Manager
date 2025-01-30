package com.germandebustamante.ringtonemanager.ui.component.common.rigtone

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.ui.component.common.button.IconPrimaryButton

@Composable
fun PlayPauseRingtoneButton(
    isPlaying: Boolean,
    onPlayPauseButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconPrimaryButton(
        onClick = onPlayPauseButtonClick,
        resourceId = if (isPlaying) R.drawable.ic_pause_ringtone else R.drawable.ic_play_ringtone,
        modifier = modifier,
    )
}