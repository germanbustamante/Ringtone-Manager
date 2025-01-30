package com.germandebustamante.ringtonemanager.ui.component.common.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.ui.theme.color_text_100
import com.germandebustamante.ringtonemanager.ui.theme.color_text_600

enum class IconPrimaryButtonSize(val dp: Dp) {
    SMALL(24.dp), MEDIUM(34.dp), BIG(44.dp)
}

@Composable
fun IconPrimaryButton(
    onClick: () -> Unit,
    @DrawableRes resourceId: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: IconPrimaryButtonSize = IconPrimaryButtonSize.BIG,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    IconButton(
        onClick = onClick,
        interactionSource = interactionSource,
        colors = getButtonColors(isPressed = isPressed),
        enabled = enabled,
        modifier = modifier.size(size.dp)
    ) {
        Icon(
            painter = painterResource(id = resourceId),
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IconPrimaryButtonPreview() {
    IconPrimaryButton(onClick = {}, resourceId = R.drawable.ic_play_ringtone, modifier = Modifier.padding(8.dp))
}

@Composable
private fun getButtonColors(isPressed: Boolean): IconButtonColors {
    val containerColor = if (isPressed) color_text_100 else Color.White

    return IconButtonDefaults.iconButtonColors(
        containerColor = containerColor,
        contentColor = color_text_600,
    )
}