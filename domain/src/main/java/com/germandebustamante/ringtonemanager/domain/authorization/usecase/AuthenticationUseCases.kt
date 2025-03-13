package com.germandebustamante.ringtonemanager.domain.authorization.usecase

import arrow.core.Either
import com.germandebustamante.ringtonemanager.domain.authorization.model.LoginTypeBO
import com.germandebustamante.ringtonemanager.domain.authorization.model.UserBO
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import kotlinx.coroutines.flow.Flow

fun interface SignInUserUseCase {
    suspend operator fun invoke(loginType: LoginTypeBO): CustomError?
}

fun interface SignOutUserUseCase : suspend () -> Unit

fun interface SignUpUserUseCase {
    suspend operator fun invoke(email: String, password: String, name: String): CustomError?
}

fun interface GetUserFlowUseCase: () -> Flow<Either<CustomError, UserBO?>>

fun interface ForgotPasswordUseCase {
    suspend operator fun invoke(email: String): CustomError?
}