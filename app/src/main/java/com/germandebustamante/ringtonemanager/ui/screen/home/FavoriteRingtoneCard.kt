package com.germandebustamante.ringtonemanager.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.ui.component.common.button.ToogleIconButton
import com.germandebustamante.ringtonemanager.ui.component.common.ringtone.PlayPauseRingtoneButton
import com.germandebustamante.ringtonemanager.ui.component.common.ringtone.RandomRingtoneBackground

private val cardWidth = 120.dp
private val thumbnailSize = 100.dp

@Composable
fun FavoriteRingtoneCard(
    ringtone: RingtoneBO,
    isPlaying: Boolean,
    onRingtoneClicked: (String) -> Unit,
    onPlayRingtoneClicked: (RingtoneBO, Boolean) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .width(cardWidth)
            .clickable { onRingtoneClicked(ringtone.id) },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.clip(MaterialTheme.shapes.small),
        ) {
            RandomRingtoneBackground(modifier = Modifier.size(thumbnailSize))
            PlayPauseRingtoneButton(
                isPlaying = isPlaying,
                onPlayPauseButtonClick = { onPlayRingtoneClicked(ringtone, isPlaying) },
            )
        }

        Text(
            text = ringtone.name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        ToogleIconButton(
            checked = true,
            onCheckedChange = { onToggleFavorite(ringtone.id) },
            activeIcon = ImageVector.vectorResource(R.drawable.ic_favorite),
            inactiveIcon = ImageVector.vectorResource(R.drawable.ic_favorite),
            activeContentDescription = stringResource(R.string.cd_remove_favorite),
            inactiveContentDescription = stringResource(R.string.cd_remove_favorite),
        )
    }
}
