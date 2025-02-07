package com.germandebustamante.ringtonemanager.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO

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
        state.isLoading,
        ringtones = state.ringtones,
        currentRingtonePlayingId = state.currentRingtonePlayingId,
        onRingtoneClicked = onRingtoneClicked,
        onPlayRingtoneClicked = onPlayRingtoneClicked,
    )
}

@Composable
fun PopularRingtoneList(
    loading: Boolean,
    ringtones: List<RingtoneBO>,
    currentRingtonePlayingId: String?,
    onRingtoneClicked: (String) -> Unit,
    onPlayRingtoneClicked: (RingtoneBO, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        if (loading) {
            items(8) {
                ShimmerPopularRingtoneItem(
                    modifier = Modifier.fillParentMaxWidth(),
                )
            }
        } else {
            items(items = ringtones, key = { it.id }) { ringtone ->
                PopularRingtoneItem(
                    ringtone = ringtone,
                    isPlaying = currentRingtonePlayingId == ringtone.id,
                    onRingtoneClicked = onRingtoneClicked,
                    onPlayRingtoneClicked = onPlayRingtoneClicked,
                    modifier = Modifier.fillParentMaxWidth(),
                )
            }
        }

    }
}