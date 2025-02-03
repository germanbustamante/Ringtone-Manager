package com.germandebustamante.ringtonemanager.ui.component.common.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.germandebustamante.ringtonemanager.ui.extension.shimmerEffect

@Composable
fun ShimmerText(
    modifier: Modifier = Modifier,
    size: TextUnit = 16.sp,
) {
    Box(
        modifier = modifier
            .size(with(LocalDensity.current) {
                size.toDp()
            })
            .widthIn(min = 40.dp)
            .shimmerEffect()
    )
}
