package com.germandebustamante.ringtonemanager.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO

@Composable
fun PopularRingtonesContainer(
    state: HomeViewModel.UIState,
    onRingtoneClicked: (String) -> Unit,
    onPlayRingtoneClicked: (RingtoneBO, Boolean) -> Unit,
    onLoadMoreClicked: () -> Unit,
) {
    Text(
        text = stringResource(R.string.popular_ringtones),
        style = MaterialTheme.typography.titleMedium,
    )

    PopularRingtoneList(
        loading = state.isLoading,
        ringtones = state.ringtones,
        currentRingtonePlayingId = state.currentRingtonePlayingId,
        canLoadMore = state.canLoadMore,
        isLoadingMore = state.isLoadingMore,
        onRingtoneClicked = onRingtoneClicked,
        onPlayRingtoneClicked = onPlayRingtoneClicked,
        onLoadMoreClicked = onLoadMoreClicked,
    )
}

@Composable
fun PopularRingtoneList(
    loading: Boolean,
    ringtones: List<RingtoneBO>,
    currentRingtonePlayingId: String?,
    canLoadMore: Boolean,
    isLoadingMore: Boolean,
    onRingtoneClicked: (String) -> Unit,
    onPlayRingtoneClicked: (RingtoneBO, Boolean) -> Unit,
    onLoadMoreClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
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

            if (canLoadMore || isLoadingMore) {
                item(key = "load_more") {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(vertical = 8.dp),
                    ) {
                        if (isLoadingMore) {
                            CircularProgressIndicator()
                        } else {
                            TextButton(onClick = onLoadMoreClicked) {
                                Text(stringResource(R.string.load_more_ringtones))
                            }
                        }
                    }
                }
            }
        }
    }
}
