package com.germandebustamante.ringtonemanager.domain.authorization.usecase

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository

class ChangePasswordUseCase(private val authenticationRepository: AuthenticationRepository) {
    suspend operator fun invoke(newPassword: String): Either<ErrorBO, Unit> =
        authenticationRepository.updatePassword(newPassword)
}
