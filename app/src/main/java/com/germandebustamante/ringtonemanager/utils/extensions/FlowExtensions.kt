package com.germandebustamante.ringtonemanager.utils.extensions

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import kotlinx.coroutines.flow.Flow

suspend fun <T> Flow<Either<ErrorBO, T>>.collectEither(
    onLeft: suspend (ErrorBO) -> Unit,
    onRight: suspend (T) -> Unit,
) = collect { eitherResult ->
    eitherResult.fold(
        ifLeft = { error -> onLeft(error) },
        ifRight = { result -> onRight(result) }
    )
}