package com.germandebustamante.ringtonemanager.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.germandebustamante.ringtonemanager.ui.component.common.ringtone.RandomRingtoneBackground
import com.germandebustamante.ringtonemanager.ui.component.common.ringtone.PlayPauseRingtoneButton
import com.germandebustamante.ringtonemanager.ui.extension.shimmerEffect

@Composable
fun ShimmerPopularRingtoneItem(
    modifier: Modifier = Modifier,
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Box(
            modifier = Modifier
                .padding(end = 8.dp)
                .clip(MaterialTheme.shapes.small)
                .size(80.dp)
                .shimmerEffect()
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 18.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect()
            )

            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraSmall)
                    .size(height = 16.dp, width = 80.dp)
                    .shimmerEffect()
            )
        }
    }
}

@Composable
fun PopularRingtoneItem(
    ringtone: RingtoneBO,
    isPlaying: Boolean,
    onRingtoneClicked: (String) -> Unit,
    onPlayRingtoneClicked: (RingtoneBO, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { onRingtoneClicked(ringtone.id) }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(end = 8.dp)
                .clip(MaterialTheme.shapes.small)
        ) {
            RandomRingtoneBackground(modifier = Modifier.size(80.dp))

            PlayPauseRingtoneButton(
                isPlaying = isPlaying,
                onPlayPauseButtonClick = { onPlayRingtoneClicked(ringtone, isPlaying) },
            )
        }

        Column {
            Text(
                text = ringtone.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
            )
            ringtone.artist?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}