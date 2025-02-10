package com.germandebustamante.ringtonemanager.ui.component.common.ringtone

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme


const val PLAY_PAUSE_BTN_SIZE_FACTOR = 1.2f

enum class IconPrimaryButtonSize(val dp: Dp) {
    SMALL(32.dp),
    MEDIUM(42.dp),
    LARGE(52.dp),
    EXTRA_LARGE(62.dp),
}

@Composable
fun PlayPauseRingtoneButton(
    isPlaying: Boolean,
    onPlayPauseButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IconPrimaryButtonSize = IconPrimaryButtonSize.MEDIUM,
) {
    val buttonSize by animateDpAsState(
        targetValue = if (!isPlaying) size.dp else size.dp * PLAY_PAUSE_BTN_SIZE_FACTOR,
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy),
        label = "Play/Pause button size animation",
    )

    FilledIconButton(
        onClick = onPlayPauseButtonClick,
        modifier = modifier.size(buttonSize),
        content = {
            val resourceId = if (isPlaying) R.drawable.ic_pause_ringtone else R.drawable.ic_play_ringtone
            Icon(
                painter = painterResource(resourceId),
                contentDescription = null,
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PlayPauseRingtoneButtonPreview() {
    var isPlaying by remember { mutableStateOf(false) }

    RingtoneManagerTheme {
        PlayPauseRingtoneButton(isPlaying = isPlaying, onPlayPauseButtonClick = { isPlaying = !isPlaying })
    }
}