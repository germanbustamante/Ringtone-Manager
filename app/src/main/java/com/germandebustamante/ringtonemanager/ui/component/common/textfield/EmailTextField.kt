package com.germandebustamante.ringtonemanager.ui.component.common.textfield

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme

@Composable
fun EmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    errorMessage: String? = null,
) {
    EditableTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        errorMessage = errorMessage,
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(
                    onClick = { onValueChange("") }
                ) {
                    Icon(Icons.Filled.Close, contentDescription = "Clear input")
                }
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
private fun EmailTextFieldPreview() {
    var text by rememberSaveable { mutableStateOf("") }
    RingtoneManagerTheme {
        EmailTextField(
            value = text,
            onValueChange = { text = it },
            label = "Email",
        )
    }
}