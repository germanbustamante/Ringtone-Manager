package com.germandebustamante.ringtonemanager.ui.component.common.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun InputErrorText(text: String, modifier: Modifier = Modifier) = Text(
    text = text,
    modifier = modifier,
    style = MaterialTheme.typography.bodyMedium,
    color = MaterialTheme.colorScheme.error
)