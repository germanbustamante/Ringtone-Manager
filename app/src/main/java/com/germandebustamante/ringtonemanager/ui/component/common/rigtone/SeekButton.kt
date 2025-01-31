package com.germandebustamante.ringtonemanager.ui.component.common.rigtone

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.ui.component.common.button.IconPrimaryButton

@Composable
fun SeekButton(
    onClick: (timeInMillis: Int) -> Unit,
    action: TimeNavigationAction,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val iconResourceId = when (action) {
        TimeNavigationAction.FORWARD_TEN_SECONDS -> R.drawable.ic_forward_10_seconds
        TimeNavigationAction.BACKWARD_TEN_SECONDS -> R.drawable.ic_backward_10_seconds
    }

    IconPrimaryButton(
        onClick = { onClick(action.timeInMillis) },
        resourceId = iconResourceId,
        enabled = enabled,
        modifier = modifier,
    )
}

enum class TimeNavigationAction(val timeInMillis: Int) {
    FORWARD_TEN_SECONDS(10000),
    BACKWARD_TEN_SECONDS(-10000),
}