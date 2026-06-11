package com.germandebustamante.ringtonemanager.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.ui.component.common.loading.ShimmerEffect

private val minHeight = 80.dp

@Composable
fun FavoriteRingtonesContainer(
    isLoading: Boolean,
    isLoggedIn: Boolean,
    favoriteRingtones: List<RingtoneBO>,
    currentRingtonePlayingId: String?,
    onRingtoneClicked: (String) -> Unit,
    onPlayRingtoneClicked: (RingtoneBO, Boolean) -> Unit,
    onToggleFavorite: (String) -> Unit,
) {
    val containerModifier = Modifier
        .fillMaxWidth()
        .defaultMinSize(minHeight = minHeight)
        .clip(MaterialTheme.shapes.small)

    Text(
        text = stringResource(R.string.favorite_ringtones),
        style = MaterialTheme.typography.titleMedium,
    )

    ShimmerEffect(loading = isLoading, modifier = containerModifier) {
        when {
            !isLoggedIn -> FavoriteEmptyBox(
                message = stringResource(R.string.favorite_rigtones_need_sign_in),
                modifier = containerModifier,
            )
            favoriteRingtones.isEmpty() -> FavoriteEmptyBox(
                message = stringResource(R.string.favorite_ringtones_empty),
                modifier = containerModifier,
            )
            else -> FavoriteRingtoneList(
                ringtones = favoriteRingtones,
                currentRingtonePlayingId = currentRingtonePlayingId,
                onRingtoneClicked = onRingtoneClicked,
                onPlayRingtoneClicked = onPlayRingtoneClicked,
                onToggleFavorite = onToggleFavorite,
            )
        }
    }
}

@Composable
private fun FavoriteEmptyBox(message: String, modifier: Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.background(MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun FavoriteRingtoneList(
    ringtones: List<RingtoneBO>,
    currentRingtonePlayingId: String?,
    onRingtoneClicked: (String) -> Unit,
    onPlayRingtoneClicked: (RingtoneBO, Boolean) -> Unit,
    onToggleFavorite: (String) -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp),
    ) {
        items(items = ringtones, key = { it.id }) { ringtone ->
            FavoriteRingtoneCard(
                ringtone = ringtone,
                isPlaying = currentRingtonePlayingId == ringtone.id,
                onRingtoneClicked = onRingtoneClicked,
                onPlayRingtoneClicked = onPlayRingtoneClicked,
                onToggleFavorite = onToggleFavorite,
            )
        }
    }
}
