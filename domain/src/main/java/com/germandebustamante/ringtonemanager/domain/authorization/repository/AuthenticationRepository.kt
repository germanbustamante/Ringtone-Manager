package com.germandebustamante.ringtonemanager.domain.authorization.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.domain.authorization.model.LoginTypeBO
import com.germandebustamante.ringtonemanager.domain.authorization.model.UserBO
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    val currentUser: Flow<Either<CustomError, UserBO?>>

    suspend fun signIn(loginType: LoginTypeBO): CustomError?
    suspend fun signUp(email: String, password: String, name: String): CustomError?
    suspend fun signOut()
    suspend fun forgotPassword(email: String): CustomError?
}