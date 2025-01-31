package com.germandebustamante.ringtonemanager.ui.screen.ringtone

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.germandebustamante.ringtonemanager.ui.component.common.RandomRingtoneBackground
import com.germandebustamante.ringtonemanager.ui.component.common.effect.DisposableEffectLifecycleObserver
import com.germandebustamante.ringtonemanager.ui.component.common.rigtone.RingtonePlayer
import org.koin.androidx.compose.koinViewModel

@Composable
fun RingtoneDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: RingtoneDetailViewModel = koinViewModel(),
) {

    DisposableEffectLifecycleObserver(
        onStop = viewModel::pausePlayer,
        onDispose = viewModel::releasePlayer,
        )

    RingtoneDetailContent(
        uiState = viewModel.uiState,
        onPlaybackPositionChange = viewModel::updatePlaybackPosition,
        onPlayPauseButtonClick = viewModel::onPlayPauseRingtone,
        onSeekButtonClick = viewModel::onSeekButtonClick,
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
private fun RingtoneDetailContent(
    uiState: RingtoneDetailViewModel.RingtoneDetailUIState,
    onPlaybackPositionChange: (Int) -> Unit,
    onPlayPauseButtonClick: () -> Unit,
    onSeekButtonClick: (timeInMillis: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 32.dp),
        ) {
            uiState.ringtone?.let { ringtone ->
                RingtoneDetails(ringtone = ringtone)

                AnimatedVisibility(uiState.ringtoneDuration != null) {
                    RingtonePlayer(
                        currentPosition = uiState.currentPlaybackPosition,
                        onPlaybackPositionChange = onPlaybackPositionChange,
                        isPlaying = uiState.isPlaying,
                        onPlayPauseButtonClick = onPlayPauseButtonClick,
                        onSeekButtonClick = onSeekButtonClick,
                        duration = uiState.ringtoneDuration!!,
                    )
                }
            }
        }
    }
}

@Composable
private fun RingtoneDetails(
    ringtone: RingtoneBO,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        RandomRingtoneBackground(
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .padding(bottom = 8.dp),
        )

        Text(
            text = ringtone.name,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )

        ringtone.artist?.let { artist ->
            Text(
                text = artist,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        ringtone.source?.let { source ->
            Text(
                text = source,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RingtoneDetailScreenPreview(
    @PreviewParameter(RingtonePreviewParametersProviders::class) uiState: RingtoneDetailViewModel.RingtoneDetailUIState,
) = RingtoneDetailContent(
    uiState = uiState,
    modifier = Modifier.fillMaxSize(),
    onPlaybackPositionChange = {},
    onPlayPauseButtonClick = {},
    onSeekButtonClick = {},
)
