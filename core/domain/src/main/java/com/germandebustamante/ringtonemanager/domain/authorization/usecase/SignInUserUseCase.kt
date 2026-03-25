package com.germandebustamante.ringtonemanager.domain.authorization.usecase

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.authorization.LoginTypeBO
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository

class SignInUserUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke(loginType: LoginTypeBO): Either<ErrorBO, Unit> =
        authenticationRepository.signIn(loginType)
}
