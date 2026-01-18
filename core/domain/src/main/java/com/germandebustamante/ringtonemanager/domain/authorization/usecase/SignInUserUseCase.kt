package com.germandebustamante.ringtonemanager.domain.authorization.usecase

import com.germandebustamante.ringtonemanager.core.model.authorization.LoginTypeBO
import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository

class SignInUserUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke(loginType: LoginTypeBO) = authenticationRepository.signIn(loginType)
}