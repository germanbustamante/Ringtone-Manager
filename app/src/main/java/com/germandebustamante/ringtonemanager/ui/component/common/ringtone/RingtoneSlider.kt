package com.germandebustamante.ringtonemanager.ui.component.common.ringtone

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RingtoneSlider(
    currentPosition: Int,
    duration: Int,
    onPlaybackPositionChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Slider(
            value = currentPosition.toFloat(),
            onValueChange = { onPlaybackPositionChange(it.toInt()) },
            valueRange = 0f..duration.toFloat(),
            enabled = false,
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = interactionSource,
                    colors = SliderDefaults.colors(
                        disabledThumbColor = MaterialTheme.colorScheme.primary,
                    ),
                    enabled = false,
                    thumbSize = DpSize(16.dp, 16.dp),
                )
            },
            colors = SliderDefaults.colors(
                disabledThumbColor = MaterialTheme.colorScheme.primary,
                disabledActiveTrackColor = MaterialTheme.colorScheme.primary,
                disabledActiveTickColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
            ),
            modifier = modifier,
        )
    }
}