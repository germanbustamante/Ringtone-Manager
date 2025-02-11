package com.germandebustamante.ringtonemanager.ui.component.common.button

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme

@Composable
fun SecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    buttonSize: ButtonSize = ButtonSize.MEDIUM,
    onClick: () -> Unit,
) = FilledTonalButton(
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

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun SecondaryButtonPreview() {
    RingtoneManagerTheme {
        SecondaryButton(text = "Secondary Button", onClick = {}, modifier = Modifier.padding(16.dp))
    }
}