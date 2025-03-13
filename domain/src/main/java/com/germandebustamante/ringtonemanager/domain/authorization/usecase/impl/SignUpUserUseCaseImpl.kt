package com.germandebustamante.ringtonemanager.domain.authorization.usecase.impl

import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignUpUserUseCase
import com.germandebustamante.ringtonemanager.domain.error.CustomError

class SignUpUserUseCaseImpl(
    private val authenticationRepository: AuthenticationRepository,
) : SignUpUserUseCase {
    override suspend fun invoke(email: String, password: String, name: String): CustomError? =
        authenticationRepository.signUp(email, password, name)
}