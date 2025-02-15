package com.germandebustamante.ringtonemanager.utils.extensions

import arrow.core.Either
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import kotlinx.coroutines.flow.Flow

suspend fun <T> Flow<Either<CustomError, T>>.collectEither(
    onLeft: suspend (CustomError) -> Unit,
    onRight: suspend (T) -> Unit,
)= this
    .collect { eitherResult ->
        eitherResult.fold(
            ifLeft = { error -> onLeft(error) },
            ifRight = { result -> onRight(result) }
        )
    }