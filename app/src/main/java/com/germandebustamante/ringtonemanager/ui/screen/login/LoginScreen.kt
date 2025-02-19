@file:OptIn(ExperimentalMaterial3Api::class)

package com.germandebustamante.ringtonemanager.ui.screen.login

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.germandebustamante.ringtonemanager.ui.component.common.textfield.EmailTextField
import com.germandebustamante.ringtonemanager.ui.component.common.textfield.PasswordTextField
import com.germandebustamante.ringtonemanager.ui.session.AccountManager
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val accountManager = remember {
        AccountManager(context)
    }

    LaunchedEffect(Unit) {
        accountManager.getCredentials(onCredentialSignInSuccess = viewModel::updateCredentials)
    }

    LoginContent(
        state = viewModel.state,
        onEmailValueChanged = viewModel::updateEmail,
        onPasswordValueChanged = viewModel::updatePassword,
        onSignInButtonClicked = viewModel::onSignInButtonClicked,
        onBackPressed = viewModel::navigateUp,
        onCleanError = viewModel::cleanError,
        onPasswordForgottenClicked = viewModel::onPasswordForgottenClicked,
        onCreateNewAccountClicked = viewModel::onCreateNewAccountClicked,
        onGoogleIdTokenReceived = viewModel::onGoogleIdTokenReceived,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun LoginContent(
    state: LoginViewModel.UIState,
    onEmailValueChanged: (String) -> Unit,
    onPasswordValueChanged: (String) -> Unit,
    onPasswordForgottenClicked: () -> Unit,
    onCreateNewAccountClicked: () -> Unit,
    onSignInButtonClicked: () -> Unit,
    onGoogleIdTokenReceived: (String) -> Unit,
    onBackPressed: () -> Unit,
    onCleanError: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val errorMessage = state.error.orEmpty()

    BaseScaffold(
        topBarTitle = stringResource(R.string.log_in),
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

            AnimatedVisibility(state.loading) {
                CircularProgressIndicator()
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                EmailTextField(
                    value = state.email.value,
                    onValueChange = onEmailValueChanged,
                    label = stringResource(R.string.input_email_title),
                    errorMessage = if (!state.email.isValid) stringResource(R.string.input_mail_error) else null,
                    modifier = Modifier.fillMaxWidth()
                )

                PasswordTextField(
                    value = state.password.value,
                    onValueChange = onPasswordValueChanged,
                    label = stringResource(R.string.login_input_password_title),
                    errorMessage = if (!state.password.isValid) stringResource(R.string.input_password_error) else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )

                TextButton(
                    onClick = onPasswordForgottenClicked,
                    contentPadding = PaddingValues(),
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Text(
                        stringResource(R.string.password_forgotten),
                    )
                }

                PrimaryButton(
                    text = stringResource(R.string.log_in),
                    onClick = onSignInButtonClicked,
                    buttonSize = ButtonSize.LARGE,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )

                ButtonGoogleSignIn(
                    onGoogleIdTokenReceived = onGoogleIdTokenReceived,
                    modifier = Modifier.padding(top = 8.dp)
                )

                TextButton(
                    onClick = onCreateNewAccountClicked,
                ) {
                    Text(
                        stringResource(R.string.login_register_text),
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }
        }
    }
}

@Preview(name = "Light Theme")
@Preview(name = "Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoginContentPreview() {
    RingtoneManagerTheme {
        LoginContent(
            onBackPressed = {},
            state = LoginViewModel.UIState(),
            onEmailValueChanged = {},
            onPasswordValueChanged = {},
            onSignInButtonClicked = {},
            onCleanError = {},
            onPasswordForgottenClicked = {},
            onCreateNewAccountClicked = {},
            onGoogleIdTokenReceived = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}