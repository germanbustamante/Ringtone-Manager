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
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.germandebustamante.ringtonemanager.ui.component.common.effect.DisposableEffectLifecycleObserver
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onRingtoneClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    HomeContent(
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
private fun HomeContent(
    state: HomeViewModel.UIState,
    onRingtoneClicked: (String) -> Unit,
    onPlayRingtoneClicked: (RingtoneBO, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        FavoriteRingtonesContainer()

        PopularRingtonesContainer(
            state = state,
            onRingtoneClicked = onRingtoneClicked,
            onPlayRingtoneClicked = onPlayRingtoneClicked,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview(
    @PreviewParameter(HomePreviewParameterProviders::class) state: HomeViewModel.UIState,
) = HomeContent(state, onRingtoneClicked = {}, onPlayRingtoneClicked = { _, _ -> })