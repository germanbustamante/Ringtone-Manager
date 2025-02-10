package com.germandebustamante.ringtonemanager.ui.component.common.ringtone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.ui.component.common.loading.ShimmerButton
import com.germandebustamante.ringtonemanager.ui.component.common.loading.ShimmerText
import com.germandebustamante.ringtonemanager.ui.extension.shimmerEffect

@Composable
fun ShimmerRingtonePlayer(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .shimmerEffect()
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        ) {
            ShimmerText(modifier = Modifier.clip(MaterialTheme.shapes.small))

            ShimmerText(modifier = Modifier.clip(MaterialTheme.shapes.small))
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {

            ShimmerButton()

            ShimmerButton(size = IconPrimaryButtonSize.EXTRA_LARGE)

            ShimmerButton()
        }
    }
}