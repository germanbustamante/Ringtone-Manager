package com.germandebustamante.ringtonemanager.ui.screen.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.germandebustamante.ringtonemanager.R

data class AppLocale(val tag: String, val displayName: String)

private val supportedLocales = listOf(
    AppLocale("en", "English"),
    AppLocale("es", "Español"),
)

@Composable
fun LanguageSelectionDialog(
    currentLanguageTag: String,
    onDismiss: () -> Unit,
    onLocaleSelected: (String) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.select_language)) },
        text = {
            Column {
                supportedLocales.forEach { locale ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLocaleSelected(locale.tag) }
                            .padding(vertical = 4.dp),
                    ) {
                        RadioButton(
                            selected = locale.tag == currentLanguageTag,
                            onClick = { onLocaleSelected(locale.tag) },
                        )
                        Text(
                            text = locale.displayName,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}

fun applyLocale(tag: String) {
    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
}
