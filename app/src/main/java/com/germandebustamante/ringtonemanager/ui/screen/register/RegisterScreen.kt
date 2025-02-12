package com.germandebustamante.ringtonemanager.ui.screen.register

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
fun RegisterScreen(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = koinViewModel(),
) {
    val state = viewModel.state

    RegisterContent(
        state = state,
        onEmailValueChanged = viewModel::updateEmail,
        onPasswordValueChanged = viewModel::updatePassword,
        onCurrentPasswordValueChanged = viewModel::updateRepeatPassword,
        onRegisterButtonClicked = viewModel::onSignInButtonClicked,
        onBackPressed = onBackPressed,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun RegisterContent(
    state: RegisterViewModel.UIState,
    onEmailValueChanged: (String) -> Unit,
    onPasswordValueChanged: (String) -> Unit,
    onCurrentPasswordValueChanged: (String) -> Unit,
    onRegisterButtonClicked: () -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BaseScaffold(
        topBarTitle = stringResource(R.string.register),
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
            )
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
            onPasswordValueChanged = {},
            onCurrentPasswordValueChanged = {},
            onRegisterButtonClicked = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}