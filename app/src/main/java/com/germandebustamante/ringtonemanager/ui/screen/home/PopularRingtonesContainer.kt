package com.germandebustamante.ringtonemanager.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.germandebustamante.ringtonemanager.ui.component.common.RandomRingtoneBackground
import com.germandebustamante.ringtonemanager.ui.component.common.rigtone.PlayPauseRingtoneButton

@Composable
fun PopularRingtonesContainer(
    state: HomeViewModel.UIState,
    onRingtoneClicked: (String) -> Unit,
    onPlayRingtoneClicked: (RingtoneBO, Boolean) -> Unit,
) {
    Text(
        text = stringResource(R.string.popular_ringtones),
        style = MaterialTheme.typography.titleMedium,
    )

    PopularRingtoneList(
        ringtones = state.ringtones,
        currentRingtonePlayingId = state.currentRingtonePlayingId,
        onRingtoneClicked = onRingtoneClicked,
        onPlayRingtoneClicked = onPlayRingtoneClicked,
    )
}

@Composable
fun PopularRingtoneList(
    ringtones: List<RingtoneBO>,
    currentRingtonePlayingId: Int?,
    onRingtoneClicked: (String) -> Unit,
    onPlayRingtoneClicked: (RingtoneBO, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(items = ringtones, key = { it.id }) { ringtone ->
            RingtoneItem(
                ringtone = ringtone,
                isPlaying = currentRingtonePlayingId == ringtone.id.toIntOrNull(),
                onRingtoneClicked = onRingtoneClicked,
                onPlayRingtoneClicked = onPlayRingtoneClicked,
                modifier = Modifier.fillParentMaxWidth(),
            )
        }
    }
}

@Composable
private fun RingtoneItem(
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