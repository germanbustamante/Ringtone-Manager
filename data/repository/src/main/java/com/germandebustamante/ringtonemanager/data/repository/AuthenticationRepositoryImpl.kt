package com.germandebustamante.ringtonemanager.data.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.authorization.LoginTypeBO
import com.germandebustamante.ringtonemanager.core.model.authorization.UserBO
import com.germandebustamante.ringtonemanager.core.model.di.DispatcherProvider
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.data.datasource.AuthenticationRemoteDataSource
import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AuthenticationRepositoryImpl(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val dispatcherProvider: DispatcherProvider,
) : AuthenticationRepository {

    override val currentUser: Flow<Either<ErrorBO, UserBO?>> =
        authenticationRemoteDataSource.currentUser.flowOn(dispatcherProvider.io)

    override suspend fun signIn(loginType: LoginTypeBO): Either<ErrorBO, Unit> = withContext(dispatcherProvider.io) {
        when (loginType) {
            is LoginTypeBO.Default -> authenticationRemoteDataSource.signIn(loginType.email, loginType.password)
            is LoginTypeBO.Google -> authenticationRemoteDataSource.googleSignIn(loginType.googleAccessToken)
        }
    }

    override suspend fun signUp(email: String, password: String, name: String): Either<ErrorBO, Unit> =
        withContext(dispatcherProvider.io) {
            authenticationRemoteDataSource.signUp(email, password, name)
        }

    override suspend fun signOut() = withContext(dispatcherProvider.io) {
        authenticationRemoteDataSource.signOut()
    }

    override suspend fun forgotPassword(email: String): Either<ErrorBO, Unit> = withContext(dispatcherProvider.io) {
        authenticationRemoteDataSource.forgotPassword(email)
    }
}
