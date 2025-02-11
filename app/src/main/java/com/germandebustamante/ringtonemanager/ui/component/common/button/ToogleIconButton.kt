package com.germandebustamante.ringtonemanager.ui.component.common.button

import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
internal fun ToogleIconButton(
    activeIcon: ImageVector,
    inactiveIcon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    IconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (checked) activeIcon else inactiveIcon,
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}