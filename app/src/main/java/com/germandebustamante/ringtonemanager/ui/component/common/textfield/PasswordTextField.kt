package com.germandebustamante.ringtonemanager.ui.component.common.textfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.ui.component.common.button.ToogleIconButton
import com.germandebustamante.ringtonemanager.ui.component.common.text.TextFieldSupportingText
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    errorMessage: String? = null,
) {
    var isFocused by rememberSaveable { mutableStateOf(false) }
    var showPassword by rememberSaveable { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.Start) {
        EditableTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .onFocusChanged { focusState -> isFocused = focusState.isFocused },
            label = label,
            errorMessage = errorMessage,
            trailingIcon = {
                ToogleIconButton(
                    activeIcon = ImageVector.vectorResource(R.drawable.hide_input_ic),
                    inactiveIcon = ImageVector.vectorResource(R.drawable.show_input_ic),
                    checked = showPassword,
                    onCheckedChange = {
                        showPassword = it
                    },
                )
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        )

        AnimatedVisibility(visible = isFocused) {
            TextFieldSupportingText(text = stringResource(R.string.input_password_error))
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun PasswordTextFieldPreview() {
    var text by rememberSaveable { mutableStateOf("") }

    RingtoneManagerTheme {
        PasswordTextField(
            value = text,
            onValueChange = { text = it },
            label = "Password",
        )
    }
}