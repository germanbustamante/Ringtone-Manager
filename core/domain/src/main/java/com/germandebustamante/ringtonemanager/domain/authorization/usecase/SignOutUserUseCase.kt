package com.germandebustamante.ringtonemanager.domain.authorization.usecase

import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository

class SignOutUserUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke() = authenticationRepository.signOut()
}