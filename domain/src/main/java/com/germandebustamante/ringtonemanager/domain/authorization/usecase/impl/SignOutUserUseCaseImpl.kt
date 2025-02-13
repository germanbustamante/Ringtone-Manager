package com.germandebustamante.ringtonemanager.domain.authorization.usecase.impl

import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.SignOutUserUseCase

class SignOutUserUseCaseImpl(
    private val authenticationRepository: AuthenticationRepository,
) : SignOutUserUseCase {
    override suspend operator fun invoke() = authenticationRepository.signOut()
}