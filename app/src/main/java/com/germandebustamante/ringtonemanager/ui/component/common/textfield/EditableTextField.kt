package com.germandebustamante.ringtonemanager.ui.component.common.textfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.germandebustamante.ringtonemanager.ui.component.common.text.InputErrorText
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme
import com.germandebustamante.ringtonemanager.utils.extensions.isTrue

@Composable
fun EditableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }

        TextFieldContentPadding(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
            isError = errorMessage?.isNotBlank().isTrue(),
            singleLine = true,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
            )
        )

        AnimatedVisibility(visible = errorMessage?.isNotBlank().isTrue()) {
            InputErrorText(text = errorMessage.orEmpty())
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditableTextFieldPreview() {
    var editableInput by rememberSaveable {
        mutableStateOf("")
    }

    RingtoneManagerTheme {
        EditableTextField(
            value = editableInput, onValueChange = { editableInput = it },
            modifier = Modifier.padding(8.dp),
            label = "Label",
        )
    }
}