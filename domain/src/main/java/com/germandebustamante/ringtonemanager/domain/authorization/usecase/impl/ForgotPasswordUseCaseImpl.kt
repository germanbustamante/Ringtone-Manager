package com.germandebustamante.ringtonemanager.domain.authorization.usecase.impl

import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.ForgotPasswordUseCase
import com.germandebustamante.ringtonemanager.domain.error.CustomError

class ForgotPasswordUseCaseImpl(
    private val authenticationRepository: AuthenticationRepository,
): ForgotPasswordUseCase {
    override suspend fun invoke(email: String): CustomError? = authenticationRepository.forgotPassword(email)

}