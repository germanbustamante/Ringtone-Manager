package com.germandebustamante.ringtonemanager.data.datasource

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.authorization.UserBO
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import kotlinx.coroutines.flow.Flow

interface AuthenticationRemoteDataSource {
    val currentUser: Flow<Either<ErrorBO, UserBO?>>

    suspend fun signIn(email: String, password: String): Either<ErrorBO, Unit>
    suspend fun googleSignIn(googleTokenId: String): Either<ErrorBO, Unit>
    suspend fun signUp(email: String, password: String, name: String): Either<ErrorBO, Unit>
    fun signOut()
    suspend fun forgotPassword(email: String): Either<ErrorBO, Unit>
}
