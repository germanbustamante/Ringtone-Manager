package com.germandebustamante.ringtonemanager.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.ui.component.common.effect.DisposableEffectLifecycleObserver
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    HomeContent(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onRingtoneClicked = viewModel::navigateToRingtoneDetail,
        onPlayRingtoneClicked = viewModel::onPlayRingtoneClicked,
        onLoadMoreClicked = viewModel::loadMoreRingtones,
        onToggleFavorite = viewModel::toggleFavorite,
        modifier = modifier,
    )

    DisposableEffectLifecycleObserver(
        onResume = viewModel::restorePlayer,
        onStop = viewModel::pausePlayer,
        onDispose = viewModel::releasePlayer,
    )
}

@Composable
internal fun HomeContent(
    state: HomeViewModel.UIState,
    onRingtoneClicked: (String) -> Unit,
    onPlayRingtoneClicked: (RingtoneBO, Boolean) -> Unit,
    onLoadMoreClicked: () -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        FavoriteRingtonesContainer(
            isLoading = state.isLoading,
            isLoggedIn = state.isLoggedIn,
            favoriteRingtones = state.favoriteRingtones,
            currentRingtonePlayingId = state.currentRingtonePlayingId,
            onRingtoneClicked = onRingtoneClicked,
            onPlayRingtoneClicked = onPlayRingtoneClicked,
            onToggleFavorite = onToggleFavorite,
        )

        PopularRingtonesContainer(
            state = state,
            onRingtoneClicked = onRingtoneClicked,
            onPlayRingtoneClicked = onPlayRingtoneClicked,
            onLoadMoreClicked = onLoadMoreClicked,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview(
    @PreviewParameter(HomePreviewParameterProviders::class) state: HomeViewModel.UIState,
) = HomeContent(
    state = state,
    onRingtoneClicked = {},
    onPlayRingtoneClicked = { _, _ -> },
    onLoadMoreClicked = {},
    onToggleFavorite = {},
)
