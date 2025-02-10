package com.germandebustamante.ringtonemanager.ui.screen.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource

@Composable
internal fun SettingsOptionCard(
    title: String,
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(SettingsScreenConstants.CardHeight),
        enabled = enabled,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = SettingsScreenConstants.DisabledAlpha)
        ),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SettingsScreenConstants.PaddingSmall),
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = SettingsScreenConstants.PaddingSmall,
                    horizontal = SettingsScreenConstants.PaddingSmall
                )
        ) {
            Icon(
                painter = painterResource(iconResId),
                contentDescription = null,
                modifier = Modifier.size(SettingsScreenConstants.IconSize)
            )
            Text(text = title)

            Spacer(modifier = Modifier.weight(1f))

            supportingText?.let {
                Text(text = it, style = MaterialTheme.typography.titleSmall)
            }
        }
    }
}