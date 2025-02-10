package com.germandebustamante.ringtonemanager.ui.component.common.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.germandebustamante.ringtonemanager.ui.component.common.ringtone.IconPrimaryButtonSize
import com.germandebustamante.ringtonemanager.ui.extension.shimmerEffect

@Composable
fun ShimmerButton(
    modifier: Modifier = Modifier,
    size: IconPrimaryButtonSize = IconPrimaryButtonSize.MEDIUM,
) = Box(
    modifier = modifier
        .size(size.dp)
        .clip(CircleShape)
        .shimmerEffect()
)