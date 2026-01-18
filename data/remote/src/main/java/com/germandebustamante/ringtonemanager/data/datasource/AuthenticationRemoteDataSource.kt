package com.germandebustamante.ringtonemanager.data.datasource

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.authorization.UserBO
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthenticationRemoteDataSource {
    val currentUser: Flow<Either<ErrorBO, UserBO?>>

    suspend fun signIn(email: String, password: String): ErrorBO?
    suspend fun googleSignIn(googleTokenId: String): Either<ErrorBO, AuthResult>
    suspend fun signUp(email: String, password: String, name: String): Either<ErrorBO, AuthResult>
    fun signOut()
    suspend fun forgotPassword(email: String): ErrorBO?
    suspend fun saveUserData(uuid: String, email: String, name: String?, loginType: String): ErrorBO?
}