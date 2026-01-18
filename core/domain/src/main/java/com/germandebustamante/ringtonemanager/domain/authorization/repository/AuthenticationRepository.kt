package com.germandebustamante.ringtonemanager.domain.authorization.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.authorization.LoginTypeBO
import com.germandebustamante.ringtonemanager.core.model.authorization.UserBO
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    val currentUser: Flow<Either<ErrorBO, UserBO?>>

    suspend fun signIn(loginType: LoginTypeBO): ErrorBO?
    suspend fun signUp(email: String, password: String, name: String): ErrorBO?
    suspend fun signOut()
    suspend fun forgotPassword(email: String): ErrorBO?
}