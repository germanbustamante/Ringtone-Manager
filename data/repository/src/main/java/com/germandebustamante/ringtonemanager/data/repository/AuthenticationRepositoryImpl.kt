package com.germandebustamante.ringtonemanager.data.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.authorization.LoginTypeBO
import com.germandebustamante.ringtonemanager.core.model.authorization.UserBO
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.data.datasource.AuthenticationRemoteDataSource
import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AuthenticationRepositoryImpl(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
) : AuthenticationRepository {

    override val currentUser: Flow<Either<ErrorBO, UserBO?>> =
        authenticationRemoteDataSource.currentUser.flowOn(Dispatchers.IO)

    override suspend fun signIn(loginType: LoginTypeBO): Either<ErrorBO, Unit> = withContext(Dispatchers.IO) {
        when (loginType) {
            is LoginTypeBO.Default -> authenticationRemoteDataSource.signIn(loginType.email, loginType.password)
            is LoginTypeBO.Google -> authenticationRemoteDataSource.googleSignIn(loginType.googleAccessToken)
        }
    }

    override suspend fun signUp(email: String, password: String, name: String): Either<ErrorBO, Unit> =
        withContext(Dispatchers.IO) {
            authenticationRemoteDataSource.signUp(email, password, name)
        }

    override suspend fun signOut() = withContext(Dispatchers.IO) {
        authenticationRemoteDataSource.signOut()
    }

    override suspend fun forgotPassword(email: String): Either<ErrorBO, Unit> = withContext(Dispatchers.IO) {
        authenticationRemoteDataSource.forgotPassword(email)
    }
}
