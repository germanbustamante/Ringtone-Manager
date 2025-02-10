package com.germandebustamante.ringtonemanager.ui.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme
import com.germandebustamante.ringtonemanager.utils.extensions.getDisplayLanguageCapitalized
import java.util.Locale

object SettingsScreenConstants {
    val PaddingSmall = 8.dp
    val PaddingMedium = 12.dp
    val CardHeight = 52.dp
    val IconSize = 32.dp
    val DisabledAlpha = 0.12f
}

@Composable
fun SettingsScreen(userLogged: Boolean, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { MyAccountTopAppBar() }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(SettingsScreenConstants.PaddingSmall)
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(SettingsScreenConstants.PaddingMedium)
        ) {
            SettingsSection(textId = R.string.language_preference) {
                SettingsOptionCard(
                    title = stringResource(R.string.change_language),
                    onClick = { /* TODO: Implement language change logic */ },
                    iconResId = R.drawable.ic_language,
                    supportingText = Locale.getDefault().getDisplayLanguageCapitalized(),
                )
            }

            SettingsSection(textId = R.string.account) {
                SettingsOptionCard(
                    title = stringResource(id = R.string.my_profile),
                    onClick = { /* TODO: Implement profile logic */ },
                    iconResId = R.drawable.ic_user_info,
                    enabled = userLogged
                )

                SettingsOptionCard(
                    title = stringResource(id = R.string.change_password),
                    onClick = { /* TODO: Implement password change logic */ },
                    iconResId = R.drawable.ic_change_password,
                    enabled = userLogged
                )
            }

            SettingsSection(textId = R.string.your_content) {
                SettingsOptionCard(
                    title = stringResource(id = R.string.your_ringtones),
                    iconResId = R.drawable.ic_ringtone,
                    enabled = userLogged,
                    onClick = { /* TODO: Implement ringtone logic */ }
                )
                SettingsOptionCard(
                    title = stringResource(id = R.string.upload_ringtone_action),
                    iconResId = R.drawable.ic_upload_ringtone,
                    enabled = userLogged,
                    onClick = { /* TODO: Implement upload logic */ }
                )
                SettingsOptionCard(
                    title = stringResource(id = R.string.favorite_ringtones),
                    iconResId = R.drawable.ic_favorite,
                    enabled = userLogged,
                    onClick = { /* TODO: Implement favorite logic */ }
                )
            }

            SettingsOptionCard(
                title = stringResource(id = if (userLogged) R.string.log_out else R.string.log_in),
                iconResId = if (userLogged) R.drawable.ic_sign_out else R.drawable.ic_sign_in,
                modifier = Modifier,
                shape = MaterialTheme.shapes.extraSmall,
                onClick = { /* TODO: Implement logout logic */ },
            )
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreviewLoggedIn() {
    RingtoneManagerTheme {
        SettingsScreen(userLogged = true, Modifier.fillMaxSize())
    }
}

@Preview
@Composable
private fun SettingsScreenPreviewLoggedOut() {
    RingtoneManagerTheme {
        SettingsScreen(userLogged = false, Modifier.fillMaxSize())
    }
}