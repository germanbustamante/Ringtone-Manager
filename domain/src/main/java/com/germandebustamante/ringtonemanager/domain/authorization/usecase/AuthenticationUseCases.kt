package com.germandebustamante.ringtonemanager.domain.authorization.usecase

import arrow.core.Either
import com.germandebustamante.ringtonemanager.domain.authorization.model.UserBO
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import kotlinx.coroutines.flow.Flow

fun interface SignInUserUseCase {
    suspend operator fun invoke(email: String, password: String): CustomError?
}

fun interface GoogleSignInUseCase {
    suspend operator fun invoke(googleTokenId: String): CustomError?
}


fun interface SignOutUserUseCase : suspend () -> Unit

fun interface SignUpUserUseCase {
    suspend operator fun invoke(email: String, password: String): CustomError?
}

fun interface GetUserFlowUseCase: () -> Flow<Either<CustomError, UserBO?>>

fun interface ForgotPasswordUseCase {
    suspend operator fun invoke(email: String): CustomError?
}