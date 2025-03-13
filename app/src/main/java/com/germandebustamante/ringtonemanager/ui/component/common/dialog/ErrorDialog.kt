package com.germandebustamante.ringtonemanager.ui.component.common.dialog

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme

@Composable
fun ErrorDialog(error: String?, onDismissRequest: () -> Unit) {
    if (error?.isNotBlank() == true) {
        BaseDialog(
            title = stringResource(id = R.string.error),
            description = error,
            acceptBtnText = stringResource(id = R.string.accept),
            onDismissRequest = onDismissRequest
        )
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
                error = "Server error occurred. Please try again later.",
                onDismissRequest = { showDialog = false }
            )
        }
    }
}