package com.germandebustamante.ringtonemanager.ui.component.common.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.germandebustamante.ringtonemanager.ui.extension.shimmerEffect

@Composable
fun ShimmerEffect(
    loading: Boolean,
    modifier: Modifier = Modifier,
    contentAfterLoading: @Composable () -> Unit,
) {
    if (loading) {
        Box(
            modifier = modifier
                .shimmerEffect()
        )
    } else {
        contentAfterLoading()
    }
}
