package com.germandebustamante.ringtonemanager.ui.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.ui.extension.surface

private val minHeight = 80.dp

@Composable
fun FavoriteRingtonesContainer() {
    Text(
        text = stringResource(R.string.favorite_ringtones),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = minHeight)
            .surface(
                shape = MaterialTheme.shapes.small,
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            )
    ) {
        Text(
            text = stringResource(R.string.favorite_rigtones_need_sign_in),
            style = MaterialTheme.typography.titleLarge,
        )
    }
}