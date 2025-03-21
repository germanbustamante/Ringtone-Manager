package com.germandebustamante.ringtonemanager.ui.screen.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.ui.component.common.button.ButtonSize
import com.germandebustamante.ringtonemanager.ui.component.common.button.PrimaryButton
import com.germandebustamante.ringtonemanager.ui.component.common.button.google.ButtonGoogleSignIn
import com.germandebustamante.ringtonemanager.ui.component.common.dialog.ErrorDialog
import com.germandebustamante.ringtonemanager.ui.component.common.scaffold.BaseScaffold
import com.germandebustamante.ringtonemanager.ui.component.common.textfield.EditableClearInput
import com.germandebustamante.ringtonemanager.ui.component.common.textfield.PasswordTextField
import com.germandebustamante.ringtonemanager.ui.session.AccountManager
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val state = viewModel.state
    val accountManager = remember {
        AccountManager(context)
    }

    LaunchedEffect(state.onUserRegistered) {
        state.onUserRegistered?.let { onUserRegistered ->
            accountManager.saveCredentials(
                email = state.email.value,
                password = state.password.value,
                onProcessFinished = onUserRegistered
            )
        }
    }

    RegisterContent(
        state = viewModel.state,
        onEmailValueChanged = viewModel::updateEmail,
        onNameValueChanged = viewModel::updateName,
        onPasswordValueChanged = viewModel::updatePassword,
        onCurrentPasswordValueChanged = viewModel::updateRepeatPassword,
        onRegisterButtonClicked = viewModel::onSignUpButtonClicked,
        onBackPressed = viewModel::navigateUp,
        onCleanError = viewModel::cleanError,
        onGoToLoginButtonClicked = viewModel::navigateToLogin,
        onGoogleIdTokenReceived = viewModel::onGoogleIdTokenReceived,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun RegisterContent(
    state: RegisterViewModel.UIState,
    onEmailValueChanged: (String) -> Unit,
    onNameValueChanged: (String) -> Unit,
    onPasswordValueChanged: (String) -> Unit,
    onCurrentPasswordValueChanged: (String) -> Unit,
    onRegisterButtonClicked: () -> Unit,
    onGoToLoginButtonClicked: () -> Unit,
    onGoogleIdTokenReceived: (String) -> Unit,
    onBackPressed: () -> Unit,
    onCleanError: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val errorMessage = state.error.orEmpty()
    BaseScaffold(
        topBarTitle = stringResource(R.string.register),
        navigationIconResource = R.drawable.ic_close,
        navigationIconClick = onBackPressed,
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            AnimatedVisibility(errorMessage.isNotBlank()) {
                ErrorDialog(
                    error = errorMessage,
                    onDismissRequest = onCleanError,
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                EditableClearInput(
                    value = state.email.value,
                    onValueChange = onEmailValueChanged,
                    label = stringResource(R.string.input_email_title),
                    errorMessage = if (!state.email.isValid) stringResource(R.string.input_mail_error) else null,
                    modifier = Modifier.fillMaxWidth()
                )

                EditableClearInput(
                    value = state.name.value,
                    onValueChange = onNameValueChanged,
                    label = stringResource(R.string.input_name_title),
                    errorMessage = if (!state.name.isValid) stringResource(R.string.input_empty_error) else null,
                    modifier = Modifier.fillMaxWidth()
                )

                PasswordTextField(
                    value = state.password.value,
                    onValueChange = onPasswordValueChanged,
                    label = stringResource(R.string.register_input_password_title),
                    errorMessage = if (!state.password.isValid) stringResource(R.string.input_password_error) else null,
                    modifier = Modifier.fillMaxWidth()
                )

                PasswordTextField(
                    value = state.repeatPassword.value,
                    onValueChange = onCurrentPasswordValueChanged,
                    label = stringResource(R.string.input_repeat_password_title),
                    errorMessage = if (!state.repeatPassword.isValid) stringResource(R.string.input_repeat_password_error) else null,
                    modifier = Modifier.fillMaxWidth()
                )

                PrimaryButton(
                    text = stringResource(R.string.register),
                    onClick = onRegisterButtonClicked,
                    buttonSize = ButtonSize.LARGE,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )

                ButtonGoogleSignIn(
                    onGoogleIdTokenReceived = onGoogleIdTokenReceived,
                    modifier = Modifier.padding(top = 8.dp)
                )

                TextButton(onClick = onGoToLoginButtonClicked) {
                    Text(
                        stringResource(R.string.register_login_text),
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }

            AnimatedVisibility(state.loading) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview
@Composable
private fun RegisterContentPreview() {
    RingtoneManagerTheme {
        RegisterContent(
            onBackPressed = {},
            state = RegisterViewModel.UIState(),
            onEmailValueChanged = {},
            onNameValueChanged = {},
            onPasswordValueChanged = {},
            onCurrentPasswordValueChanged = {},
            onRegisterButtonClicked = {},
            onCleanError = {},
            onGoToLoginButtonClicked = {},
            onGoogleIdTokenReceived = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}