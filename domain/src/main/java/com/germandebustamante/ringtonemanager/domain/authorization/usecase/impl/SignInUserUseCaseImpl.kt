package com.germandebustamante.ringtonemanager.domain.authorization.usecase.impl

import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignInUserUseCase

class SignInUserUseCaseImpl(
    private val authenticationRepository: AuthenticationRepository,
) : SignInUserUseCase {
    override suspend fun invoke(email: String, password: String) =
        authenticationRepository.signIn(email, password)
}