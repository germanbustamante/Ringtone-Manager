package com.germandebustamante.ringtonemanager.ui.screen.ringtone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.germandebustamante.ringtonemanager.ui.component.common.action.ShareButtonWithToolTip
import com.germandebustamante.ringtonemanager.ui.component.common.effect.DisposableEffectLifecycleObserver
import com.germandebustamante.ringtonemanager.ui.component.common.ringtone.RandomRingtoneBackground
import com.germandebustamante.ringtonemanager.ui.component.common.ringtone.RingtonePlayer
import com.germandebustamante.ringtonemanager.ui.component.common.ringtone.ShimmerRingtonePlayer
import com.germandebustamante.ringtonemanager.ui.component.common.scaffold.BaseScaffold
import org.koin.androidx.compose.koinViewModel

@Composable
fun RingtoneDetailScreen(
    onBackPressed: () -> Unit,
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
        onBackPressed = onBackPressed,
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
private fun RingtoneDetailContent(
    uiState: RingtoneDetailViewModel.RingtoneDetailUIState,
    onPlaybackPositionChange: (Int) -> Unit,
    onPlayPauseButtonClick: () -> Unit,
    onSeekButtonClick: (timeInMillis: Int) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BaseScaffold(
        topBarTitle = uiState.ringtone?.name,
        navigationIconResource = R.drawable.ic_back,
        navigationIconClick = onBackPressed,

        ) { _ ->
        Box(
            modifier = modifier.padding(6.dp),
        ) {
            val innerIconsHorizontalPadding = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.align(Alignment.Center),
            ) {
                uiState.ringtone?.let { ringtone ->

                    RingtoneDetails(ringtone = ringtone, modifier = innerIconsHorizontalPadding)

                    if (uiState.ringtoneDuration != null) {
                        RingtonePlayer(
                            currentPosition = uiState.currentPlaybackPosition,
                            onPlaybackPositionChange = onPlaybackPositionChange,
                            isPlaying = uiState.isPlaying,
                            onPlayPauseButtonClick = onPlayPauseButtonClick,
                            onSeekButtonClick = onSeekButtonClick,
                            duration = uiState.ringtoneDuration,
                            modifier = innerIconsHorizontalPadding,
                        )
                    } else {
                        ShimmerRingtonePlayer(
                            modifier = innerIconsHorizontalPadding,
                        )
                    }
                }
            }

            ShareButtonWithToolTip(
                onClick = {},
                descriptionText = stringResource(R.string.share_action),
                modifier = Modifier
                    .align(Alignment.BottomCenter),
            )
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
        verticalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        RandomRingtoneBackground(
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
        )

        Text(
            text = ringtone.name,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp),
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
                style = MaterialTheme.typography.bodySmall,
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
    onBackPressed = {},
)
