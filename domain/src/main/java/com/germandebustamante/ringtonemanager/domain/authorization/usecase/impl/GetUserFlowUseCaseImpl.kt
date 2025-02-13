package com.germandebustamante.ringtonemanager.domain.authorization.usecase.impl

import com.germandebustamante.ringtonemanager.domain.authorization.model.UserBO
import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.GetUserFlowUseCase
import kotlinx.coroutines.flow.Flow

class GetUserFlowUseCaseImpl (
    private val authenticationRepository: AuthenticationRepository
) : GetUserFlowUseCase {
    override fun invoke(): Flow<UserBO?> = authenticationRepository.currentUser
}