package com.germandebustamante.ringtonemanager.domain.authorization.usecase

import com.germandebustamante.ringtonemanager.domain.authorization.model.UserBO
import kotlinx.coroutines.flow.Flow

fun interface SignInUserUseCase {
    suspend operator fun invoke(email: String, password: String)
}

fun interface SignOutUserUseCase : suspend () -> Unit

fun interface SignUpUserUseCase {
    suspend operator fun invoke(email: String, password: String)
}

fun interface GetUserFlowUseCase: () -> Flow<UserBO?>