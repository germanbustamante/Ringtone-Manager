package com.germandebustamante.ringtonemanager.domain.authorization.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.authorization.LoginTypeBO
import com.germandebustamante.ringtonemanager.core.model.authorization.UserBO
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    val currentUser: Flow<Either<ErrorBO, UserBO?>>

    suspend fun signIn(loginType: LoginTypeBO): Either<ErrorBO, Unit>
    suspend fun signUp(email: String, password: String, name: String): Either<ErrorBO, Unit>
    suspend fun signOut()
    suspend fun forgotPassword(email: String): Either<ErrorBO, Unit>
    suspend fun updatePassword(newPassword: String): Either<ErrorBO, Unit>
}
