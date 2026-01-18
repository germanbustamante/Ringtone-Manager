package com.germandebustamante.ringtonemanager.domain.authorization.usecase

import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository

class ForgotPasswordUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke(email: String): ErrorBO? = authenticationRepository.forgotPassword(email)
}