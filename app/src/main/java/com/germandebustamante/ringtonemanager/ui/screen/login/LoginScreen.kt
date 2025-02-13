package com.germandebustamante.ringtonemanager.ui.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.ui.component.common.button.ButtonSize
import com.germandebustamante.ringtonemanager.ui.component.common.button.PrimaryButton
import com.germandebustamante.ringtonemanager.ui.component.common.scaffold.BaseScaffold
import com.germandebustamante.ringtonemanager.ui.component.common.textfield.EmailTextField
import com.germandebustamante.ringtonemanager.ui.component.common.textfield.PasswordTextField
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state = viewModel.state

    LoginContent(
        state = state,
        onEmailValueChanged = viewModel::updateEmail,
        onPasswordValueChanged = viewModel::updatePassword,
        onSignInButtonClicked = viewModel::onSignInButtonClicked,
        onBackPressed = viewModel::navigateUp,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun LoginContent(
    state: LoginViewModel.UIState,
    onEmailValueChanged: (String) -> Unit,
    onPasswordValueChanged: (String) -> Unit,
    onSignInButtonClicked: () -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BaseScaffold(
        topBarTitle = stringResource(R.string.log_in),
        navigationIconResource = R.drawable.ic_close,
        navigationIconClick = onBackPressed,
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
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
                label = stringResource(R.string.input_password_title),
                errorMessage = if (!state.password.isValid) stringResource(R.string.input_password_error) else null,
                modifier = Modifier.fillMaxWidth()
            )

            PrimaryButton(
                text = stringResource(R.string.log_in),
                onClick = onSignInButtonClicked,
                buttonSize = ButtonSize.LARGE,
            )
        }
    }
}

@Preview
@Composable
private fun LoginContentPreview() {
    RingtoneManagerTheme {
        LoginContent(
            onBackPressed = {},
            state = LoginViewModel.UIState(),
            onEmailValueChanged = {},
            onPasswordValueChanged = {},
            onSignInButtonClicked = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}