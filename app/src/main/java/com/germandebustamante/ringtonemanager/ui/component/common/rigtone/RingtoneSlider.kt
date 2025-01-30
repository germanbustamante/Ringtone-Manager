package com.germandebustamante.ringtonemanager.ui.component.common.rigtone

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    Slider(
        value = currentPosition.toFloat(),
        onValueChange = { onPlaybackPositionChange(it.toInt()) },
        valueRange = 0f..duration.toFloat(),
        enabled = false,
        thumb = {
            SliderDefaults.Thumb(
                interactionSource = interactionSource,
                colors = SliderDefaults.colors(
                    disabledThumbColor = Color.White,
                ),
                enabled = false,
                thumbSize = DpSize(18.dp, 18.dp),
            )
        },
        colors = SliderDefaults.colors(
            disabledThumbColor = Color.White,
            disabledActiveTrackColor = Color.White,
        ),
        modifier = modifier,
    )
}