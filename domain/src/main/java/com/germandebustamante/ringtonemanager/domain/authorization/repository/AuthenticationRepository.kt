package com.germandebustamante.ringtonemanager.domain.authorization.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.domain.authorization.model.UserBO
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    val currentUser: Flow<Either<CustomError, UserBO?>>

    suspend fun signIn(email: String, password: String): CustomError?
    suspend fun signUp(email: String, password: String): CustomError?
    suspend fun signOut()
}