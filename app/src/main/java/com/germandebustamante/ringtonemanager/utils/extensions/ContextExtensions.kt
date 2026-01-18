package com.germandebustamante.ringtonemanager.utils.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO

@Composable
fun ErrorBO.errorString(): String = when (this) {
    is ErrorBO.Server -> message ?: stringResource(R.string.server_error)
    is ErrorBO.ParcelizeException -> stringResource(R.string.parcelize_error)
    is ErrorBO.Unknown -> stringResource(R.string.unknown_error)
    is ErrorBO.EmailAddressAlreadyInUse -> stringResource(R.string.email_address_already_in_use)
    is ErrorBO.NotFound -> stringResource(R.string.item_not_found)
    is ErrorBO.InvalidCredentials -> stringResource(R.string.email_or_password_incorrect)
}
