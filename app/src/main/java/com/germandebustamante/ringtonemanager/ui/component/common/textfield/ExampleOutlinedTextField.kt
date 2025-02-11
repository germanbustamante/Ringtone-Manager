package com.germandebustamante.ringtonemanager.ui.component.common.textfield
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconToggleButton
//import androidx.compose.material3.LocalTextStyle
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.onFocusChanged
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.res.vectorResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.germandebustamante.ringtonemanager.R
//import com.germandebustamante.ringtonemanager.ui.component.common.text.InputErrorText
//import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme

//
//@Composable
//fun OutlinedTextField(
//    value: String,
//    onValueChange: (String) -> Unit,
//    modifier: Modifier = Modifier,
//    inputText: String? = null,
//    label: String? = null,
//    errorMessage: String? = null,
//    supportingText: String? = null,
//    enabled: Boolean = true,
//    isError: Boolean = false,
//    visualTransformation: VisualTransformation = VisualTransformation.None,
//    trailingIcon: @Composable (() -> Unit)? = null,
//) {
//    var isFocused by rememberSaveable { mutableStateOf(false) }
//
//    Column(modifier = modifier) {
//        if (label != null) {
//            Text(
//                text = label,
//                style = MaterialTheme.typography.bodyMedium,
//            )
//        }
//
//        OutlinedTextField(
//            value = value,
//            onValueChange = onValueChange,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 4.dp)
//                .onFocusChanged { focusState -> isFocused = focusState.isFocused },
//            trailingIcon = trailingIcon,
//            singleLine = true,
//            isError = isError,
//            enabled = enabled,
//            textStyle = LocalTextStyle.current.copy(
//                fontSize = 16.sp,
//                fontWeight = FontWeight(400),
//                lineHeight = 24.sp,
//            ),
//            label = {
//                if (!isFocused && inputText != null) {
//                    Text(
//                        text = inputText,
//                        style = MaterialTheme.typography.bodyLarge,
//                    )
//                }
//            },
//            visualTransformation = visualTransformation,
//            shape = RoundedCornerShape(6.dp),
//        )
//
//        AnimatedVisibility(visible = supportingText != null && isFocused) {
//            TextFieldSupportingText(text = supportingText!!)
//        }
//
//        AnimatedVisibility(isError && errorMessage != null) {
//            InputErrorText(text = errorMessage.orEmpty())
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//private fun OutlinedTextFieldPreview() {
//    var inputValue by rememberSaveable { mutableStateOf("") }
//
//    RingtoneManagerTheme {
//        OutlinedTextField(
//            value = inputValue,
//            onValueChange = { inputValue = it },
//            label = "Label",
//            inputText = "Input Text",
//            supportingText = "Supporting Text",
//        )
//    }
//}