package com.germandebustamante.ringtonemanager.ui.component.common.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class ButtonSize(val height: Int, val minWidth: Int, val horizontalPadding: Int, val verticalPadding: Int) {
    LARGE(height = 44, minWidth = 180, horizontalPadding = 32, verticalPadding = 8),
    MEDIUM(height = 40, minWidth = 120, horizontalPadding = 16, verticalPadding = 10),
    SMALL(height = 36, minWidth = 60, horizontalPadding = 16, verticalPadding = 8)
}

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    buttonSize: ButtonSize = ButtonSize.MEDIUM,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .defaultMinSize(minWidth = buttonSize.minWidth.dp)
            .height(buttonSize.height.dp),
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(
            horizontal = buttonSize.horizontalPadding.dp,
            vertical = buttonSize.verticalPadding.dp
        )
    ) {
        Text(text = text, style = MaterialTheme.typography.titleMedium)
    }
}