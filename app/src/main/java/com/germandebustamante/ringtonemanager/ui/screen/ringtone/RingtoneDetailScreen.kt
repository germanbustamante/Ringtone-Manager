package com.germandebustamante.ringtonemanager.ui.screen.ringtone

import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.ui.component.common.action.ShareButtonWithToolTip
import com.germandebustamante.ringtonemanager.ui.component.common.effect.DisposableEffectLifecycleObserver
import com.germandebustamante.ringtonemanager.ui.component.common.ringtone.RandomRingtoneBackground
import com.germandebustamante.ringtonemanager.ui.component.common.ringtone.RingtonePlayer
import com.germandebustamante.ringtonemanager.ui.component.common.ringtone.ShimmerRingtonePlayer
import com.germandebustamante.ringtonemanager.ui.component.common.scaffold.BaseScaffold
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RingtoneDetailScreen(
    route: Destination.RingtoneDetailScreen,
    modifier: Modifier = Modifier,
    viewModel: RingtoneDetailViewModel = koinViewModel(parameters = { parametersOf(route) }),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val ringtoneSetSuccessMessage = stringResource(R.string.ringtone_set_success)

    // After returning from WRITE_SETTINGS system screen, re-attempt if permission is now granted.
    val writeSettingsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) {
        if (Settings.System.canWrite(context)) viewModel.setAsRingtone()
    }

    LaunchedEffect(uiState.isRingtoneSet) {
        if (uiState.isRingtoneSet) snackbarHostState.showSnackbar(ringtoneSetSuccessMessage)
    }

    DisposableEffectLifecycleObserver(
        onStop = viewModel::pausePlayer,
        onDispose = viewModel::releasePlayer,
    )

    RingtoneDetailContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onPlaybackPositionChange = viewModel::updatePlaybackPosition,
        onPlayPauseButtonClick = viewModel::onPlayPauseRingtone,
        onSeekButtonClick = viewModel::onSeekButtonClick,
        onBackPressed = viewModel::navigateUp,
        onToggleFavoriteClicked = viewModel::toggleFavorite,
        onShareClicked = {
            uiState.ringtone?.let { ringtone ->
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, ringtone.name)
                    putExtra(Intent.EXTRA_TEXT, ringtone.fileUrl)
                }
                context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_action)))
            }
        },
        onSetAsRingtoneClicked = {
            if (Settings.System.canWrite(context)) {
                viewModel.setAsRingtone()
            } else {
                writeSettingsLauncher.launch(
                    Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                        data = android.net.Uri.parse("package:${context.packageName}")
                    },
                )
            }
        },
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
internal fun RingtoneDetailContent(
    uiState: RingtoneDetailViewModel.RingtoneDetailUIState,
    snackbarHostState: SnackbarHostState,
    onPlaybackPositionChange: (Int) -> Unit,
    onPlayPauseButtonClick: () -> Unit,
    onSeekButtonClick: (timeInMillis: Int) -> Unit,
    onBackPressed: () -> Unit,
    onToggleFavoriteClicked: () -> Unit,
    onShareClicked: () -> Unit,
    onSetAsRingtoneClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BaseScaffold(
        topBarTitle = uiState.ringtone?.name,
        navigationIconResource = R.drawable.ic_back,
        navigationIconClick = onBackPressed,
        snackbarHost = { SnackbarHost(snackbarHostState) },
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

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
            ) {
                OutlinedButton(
                    onClick = onSetAsRingtoneClicked,
                    enabled = uiState.ringtone != null && !uiState.isSettingRingtone,
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_ringtone),
                        contentDescription = null,
                        modifier = Modifier.padding(end = 6.dp),
                    )
                    Text(stringResource(R.string.set_as_ringtone))
                }

                if (uiState.isLoggedIn) {
                    IconButton(onClick = onToggleFavoriteClicked) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_favorite),
                            contentDescription = stringResource(
                                if (uiState.isFavorite) R.string.cd_remove_favorite else R.string.cd_add_favorite,
                            ),
                            tint = if (uiState.isFavorite) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            },
                        )
                    }
                }

                ShareButtonWithToolTip(
                    onClick = onShareClicked,
                    descriptionText = stringResource(R.string.share_action),
                )
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
        verticalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        RandomRingtoneBackground(
            modifier = Modifier.clip(MaterialTheme.shapes.small),
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
    snackbarHostState = remember { SnackbarHostState() },
    modifier = Modifier.fillMaxSize(),
    onPlaybackPositionChange = {},
    onPlayPauseButtonClick = {},
    onSeekButtonClick = {},
    onBackPressed = {},
    onToggleFavoriteClicked = {},
    onShareClicked = {},
    onSetAsRingtoneClicked = {},
)
