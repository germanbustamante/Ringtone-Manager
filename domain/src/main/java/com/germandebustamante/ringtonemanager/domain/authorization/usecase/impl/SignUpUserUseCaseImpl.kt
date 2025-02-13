package com.germandebustamante.ringtonemanager.domain.authorization.usecase.impl

import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignUpUserUseCase

class SignUpUserUseCaseImpl(
    private val authenticationRepository: AuthenticationRepository,
) : SignUpUserUseCase {
    override suspend fun invoke(email: String, password: String) =
        authenticationRepository.signUp(email, password)
}