package com.germandebustamante.ringtonemanager.ui.screen.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
) {
    LoginContent()
}

@Composable
private fun LoginContent() {
    //TODO
}

@Preview
@Composable
private fun LoginContentPreview() {
    RingtoneManagerTheme {
        LoginContent()
    }
}