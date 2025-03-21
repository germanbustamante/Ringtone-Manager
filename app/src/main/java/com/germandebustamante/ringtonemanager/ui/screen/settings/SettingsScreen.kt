package com.germandebustamante.ringtonemanager.ui.screen.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.ui.component.common.dialog.ErrorDialog
import com.germandebustamante.ringtonemanager.ui.component.common.scaffold.BaseScaffold
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme
import com.germandebustamante.ringtonemanager.utils.extensions.getDisplayLanguageCapitalized
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

object SettingsScreenConstants {
    val PaddingSmall = 8.dp
    val PaddingMedium = 12.dp
    val CardHeight = 52.dp
    val IconSize = 32.dp
    val DisabledAlpha = 0.12f
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val state = viewModel.state

    SettingsContent(
        state = state,
        onSignInClicked = viewModel::navigateToSignIn,
        onSignOutClicked = viewModel::signOut,
        onRegisterClicked = viewModel::navigateToSignUp,
        onCleanError = viewModel::cleanError,
        modifier = modifier,
    )
}

@Composable
private fun SettingsContent(
    state: SettingsViewModel.UIState,
    onSignInClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    onRegisterClicked: () -> Unit,
    onCleanError: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val errorMessage = state.error.orEmpty()
    val userLogged = state.userLogged

    BaseScaffold(
        topBarTitle = stringResource(R.string.account_configuration),
    ) { innerPadding ->

        AnimatedVisibility(errorMessage.isNotBlank()) {
            ErrorDialog(
                error = state.error,
                onDismissRequest = onCleanError
            )
        }

        AnimatedVisibility(state.isLoading) {
            CircularProgressIndicator()
        }

        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(SettingsScreenConstants.PaddingSmall)
                .padding(top = 8.dp)
                .fillMaxSize(),
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
                onClick = if (userLogged) onSignOutClicked else onSignInClicked,
            )

            if (!userLogged) {
                SettingsOptionCard(
                    title = stringResource(id = R.string.register),
                    iconResId = R.drawable.ic_register,
                    onClick = onRegisterClicked,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreviewLoggedIn() {
    RingtoneManagerTheme {
        SettingsContent(
            state = SettingsViewModel.UIState(userLogged = true),
            onSignInClicked = {},
            onSignOutClicked = {},
            onRegisterClicked = {},
            onCleanError = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun SettingsScreenPreviewLoggedOut() {
    RingtoneManagerTheme {
        SettingsContent(
            SettingsViewModel.UIState(userLogged = false),
            onSignInClicked = {},
            onSignOutClicked = {},
            onRegisterClicked = {},
            onCleanError = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}