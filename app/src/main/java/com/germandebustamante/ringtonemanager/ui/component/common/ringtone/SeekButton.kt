package com.germandebustamante.ringtonemanager.ui.component.common.ringtone

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.germandebustamante.ringtonemanager.R

@Composable
fun SeekButton(
    onClick: (timeInMillis: Int) -> Unit,
    action: TimeNavigationAction,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val iconResourceId = when (action) {
        TimeNavigationAction.FORWARD_TEN_SECONDS -> R.drawable.ic_forward_10_seconds
        TimeNavigationAction.BACKWARD_TEN_SECONDS -> R.drawable.ic_backward_10_seconds
    }

    IconButton(
        onClick = { onClick(action.timeInMillis) },
        enabled = enabled,
        modifier = modifier.size(IconPrimaryButtonSize.MEDIUM.dp),
    ) {
        Icon(
            painter = painterResource(iconResourceId),
            contentDescription = null,
        )
    }
}

enum class TimeNavigationAction(val timeInMillis: Int) {
    FORWARD_TEN_SECONDS(10000),
    BACKWARD_TEN_SECONDS(-10000),
}