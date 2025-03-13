package com.germandebustamante.ringtonemanager.data.datasource

import arrow.core.Either
import com.germandebustamante.ringtonemanager.domain.authorization.model.UserBO
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthenticationRemoteDataSource {
    val currentUser: Flow<Either<CustomError, UserBO?>>

    suspend fun signIn(email: String, password: String): CustomError?
    suspend fun googleSignIn(googleTokenId: String): Either<CustomError, AuthResult>
    suspend fun signUp(email: String, password: String, name: String): Either<CustomError, AuthResult>
    fun signOut()
    suspend fun forgotPassword(email: String): CustomError?
    suspend fun saveUserData(uuid: String, email: String, name: String?, loginType: String): CustomError?
}