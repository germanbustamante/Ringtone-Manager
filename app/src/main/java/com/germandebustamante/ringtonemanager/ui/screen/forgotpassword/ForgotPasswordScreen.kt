package com.germandebustamante.ringtonemanager.ui.screen.forgotpassword

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.ui.base.ValidatorInputState
import com.germandebustamante.ringtonemanager.ui.component.common.button.ButtonSize
import com.germandebustamante.ringtonemanager.ui.component.common.button.PrimaryButton
import com.germandebustamante.ringtonemanager.ui.component.common.dialog.ErrorDialog
import com.germandebustamante.ringtonemanager.ui.component.common.scaffold.BaseScaffold
import com.germandebustamante.ringtonemanager.ui.component.common.textfield.EditableClearInput
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = koinViewModel(),
) {
    ForgotPasswordContent(
        state = viewModel.state,
        onBackPressed = viewModel::navigateUp,
        onCleanError = viewModel::cleanError,
        onEmailValueChanged = viewModel::updateEmail,
        onRestorePasswordBtnClicked = viewModel::onRestorePasswordClicked,
    )
}

@Composable
private fun ForgotPasswordContent(
    state: ForgotPasswordViewModel.UIState,
    onBackPressed: () -> Unit,
    onCleanError: () -> Unit,
    onEmailValueChanged: (String) -> Unit,
    onRestorePasswordBtnClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val errorMessage = state.error.orEmpty()

    BaseScaffold(
        topBarTitle = stringResource(R.string.password_forgotten),
        navigationIconResource = R.drawable.ic_back,
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
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(vertical = 32.dp)
            ) {
                Text(
                    text = stringResource(R.string.restore_password_description),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp),
                )

                EditableClearInput(
                    value = state.email.value,
                    onValueChange = onEmailValueChanged,
                    label = stringResource(R.string.input_email_title),
                    errorMessage = if (!state.email.isValid) stringResource(R.string.input_mail_error) else null,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(1f))

                PrimaryButton(
                    text = stringResource(R.string.restore_password_action),
                    onClick = onRestorePasswordBtnClicked,
                    buttonSize = ButtonSize.LARGE,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
fun ForgotPasswordScreenPreview() {
    var email by remember { mutableStateOf("") }
    RingtoneManagerTheme {
        ForgotPasswordContent(
            state = ForgotPasswordViewModel.UIState(
                email = ValidatorInputState(email),
            ),
            onBackPressed = {},
            onCleanError = {},
            onEmailValueChanged = { email = it },
            onRestorePasswordBtnClicked = {},
        )
    }
}