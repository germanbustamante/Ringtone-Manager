//package com.germandebustamante.ringtonemanager.ui.component.common.textfield
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.res.vectorResource
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.tooling.preview.Preview
//import com.germandebustamante.ringtonemanager.R
//import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme
//
///**
// * Displays an input field for entering a password with optional error state, label, error message,
// * supporting text, and toggle visibility button for showing/hiding the password.
// *
// *
// * @param value The current value of the password input field.
// * @param onValueChange Lambda function invoked when the value of the input field changes.
// * @param modifier Optional modifier for this layout. Defaults to Modifier.
// * @param isError Flag indicating if the input field should display an error state.
// * @param inputText Optional text content type for the input field.
// * @param label Optional label text displayed above the input field.
// * @param errorMessage Optional error message text displayed when isError is true.
// * @param supportingText Optional supporting text displayed below the input field.
// * @param enabled Flag indicating if the input field is enabled for user interaction. Defaults to true.
// */
//@Composable
//fun PasswordTextField(
//    value: String,
//    onValueChange: (String) -> Unit,
//    modifier: Modifier = Modifier,
//    isError: Boolean = false,
//    inputText: String? = null,
//    label: String? = null,
//    errorMessage: String? = null,
//    supportingText: String? = null,
//    enabled: Boolean = true,
//) {
//    var showInput by rememberSaveable { mutableStateOf(false) }
//
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        modifier = modifier,
//        inputText = inputText,
//        label = label,
//        errorMessage = errorMessage,
//        supportingText = supportingText,
//        enabled = enabled,
//        isError = isError,
//        visualTransformation = if (showInput) VisualTransformation.None else PasswordVisualTransformation(),
//        trailingIcon = {
//            ToogleIcon(
//                activeIcon = ImageVector.vectorResource(R.drawable.hide_input_ic),
//                inactiveIcon = ImageVector.vectorResource(R.drawable.show_input_ic),
//                checked = showInput,
//                onCheckedChange = {
//                    showInput = it
//                },
//            )
//        }
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//private fun PasswordInputPreview() {
//    RingtoneManagerTheme {
//        var inputValue by rememberSaveable { mutableStateOf("") }
//
//        PasswordTextField(
//            value = inputValue,
//            onValueChange = { inputValue = it },
//            label = "Password label",
//            inputText = "Password Input Text",
//            supportingText = "Añadir aquí información para ayudar al usuario a entender este input"
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//private fun PasswordInputErrorPreview() {
//    RingtoneManagerTheme {
//        PasswordTextField(
//            value = "",
//            onValueChange = { },
//            isError = true,
//            errorMessage = "Formato invalido"
//        )
//    }
//}