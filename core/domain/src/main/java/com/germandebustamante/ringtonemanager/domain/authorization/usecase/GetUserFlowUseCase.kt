package com.germandebustamante.ringtonemanager.domain.authorization.usecase

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.authorization.UserBO
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow

class GetUserFlowUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(): Flow<Either<ErrorBO, UserBO?>> = authenticationRepository.currentUser
}