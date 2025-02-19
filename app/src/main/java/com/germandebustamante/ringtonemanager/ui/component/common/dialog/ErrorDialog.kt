package com.germandebustamante.ringtonemanager.ui.component.common.dialog

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.ui.component.common.button.PrimaryButton
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme

@Composable
fun ErrorDialog(error: String?, onDismissRequest: () -> Unit) {
    if (error?.isNotBlank() == true) {
        ErrorDialog(
            title = stringResource(id = R.string.error),
            description = error,
            acceptBtnText = stringResource(id = R.string.accept),
            onDismissRequest = onDismissRequest
        )
    }
}

@Composable
private fun ErrorDialog(
    title: String,
    description: String,
    acceptBtnText: String,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 28.dp, start = 32.dp, end = 32.dp),
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 12.dp, start = 32.dp, end = 32.dp)
                    )

                    PrimaryButton(
                        text = acceptBtnText,
                        onClick = onDismissRequest,
                        modifier = Modifier.padding(vertical = 32.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ErrorDialogPreview() {
    var showDialog by rememberSaveable {
        mutableStateOf(false)
    }
    Box(modifier = Modifier.padding(40.dp)) {
        TextButton(onClick = { showDialog = !showDialog }) {
            Text(text = "Show dialog")
        }
    }

    if (showDialog) {
        RingtoneManagerTheme {
            ErrorDialog(
                "Oops!",
                "Server error occurred. Please try again later.",
                "Accept",
                onDismissRequest = { showDialog = false })
        }
    }
}