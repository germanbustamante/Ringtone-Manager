package com.germandebustamante.ringtonemanager.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.germandebustamante.ringtonemanager.ui.component.common.RandomRingtoneBackground
import com.germandebustamante.ringtonemanager.ui.component.common.ToogleIcon
import com.germandebustamante.ringtonemanager.ui.component.common.effect.DisposableEffectLifecycleObserver
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onRingtoneClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    HomeScreen(
        state = viewModel.state,
        onRingtoneClicked = onRingtoneClicked,
        onPlayRingtoneClicked = viewModel::onPlayRingtoneClicked,
        modifier = modifier
    )

    DisposableEffectLifecycleObserver(
        onResume = viewModel::restorePlayer,
        onStop = viewModel::pausePlayer,
        onDispose = viewModel::releasePlayer,
    )
}

@Composable
private fun HomeScreen(
    state: HomeViewModel.UIState,
    onRingtoneClicked: (String) -> Unit,
    onPlayRingtoneClicked: (RingtoneBO, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .padding(8.dp)
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(items = state.ringtones, key = { it.id }) { ringtone ->
                    RingtoneItem(
                        ringtone = ringtone,
                        isPlaying = state.currentRingtonePlayingId == ringtone.id.toIntOrNull(),
                        onRingtoneClicked = onRingtoneClicked,
                        onPlayRingtoneClicked = onPlayRingtoneClicked,
                        modifier = Modifier.fillParentMaxWidth(),
                    )
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator()
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
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { onRingtoneClicked(ringtone.id) }
    ) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .padding(end = 8.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            RandomRingtoneBackground()
            ToogleIcon(
                activeIcon = ImageVector.vectorResource(id = R.drawable.ic_play_ringtone),
                inactiveIcon = ImageVector.vectorResource(id = R.drawable.ic_pause_ringtone),
                checked = !isPlaying,
                onCheckedChange = { onPlayRingtoneClicked(ringtone, it) },
                modifier = Modifier.size(48.dp),
            )
        }

        Column {
            Text(
                text = ringtone.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
            ringtone.artist?.let { Text(text = it, style = MaterialTheme.typography.bodySmall) }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview(
    @PreviewParameter(HomePreviewParameterProviders::class) state: HomeViewModel.UIState,
) = HomeScreen(state, onRingtoneClicked = {}, onPlayRingtoneClicked = { _, _ -> })