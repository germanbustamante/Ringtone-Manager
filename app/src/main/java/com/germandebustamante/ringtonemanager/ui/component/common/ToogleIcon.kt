package com.germandebustamante.ringtonemanager.ui.component.common

import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.germandebustamante.ringtonemanager.R

@Composable
fun ToogleIcon(
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

@Preview(showBackground = true)
@Composable
private fun ToogleIconPreview() {
    var checked by remember { mutableStateOf(false) }
    ToogleIcon(
        activeIcon = ImageVector.vectorResource(R.drawable.ic_play_ringtone),
        inactiveIcon = ImageVector.vectorResource(R.drawable.ic_pause_ringtone),
        checked = checked,
        onCheckedChange = { checked = it }
    )
}