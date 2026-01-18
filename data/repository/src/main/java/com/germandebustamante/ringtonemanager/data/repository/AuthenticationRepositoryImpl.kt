package com.germandebustamante.ringtonemanager.data.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.authorization.LoginTypeBO
import com.germandebustamante.ringtonemanager.core.model.authorization.UserBO
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.data.datasource.AuthenticationRemoteDataSource
import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AuthenticationRepositoryImpl(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
) : AuthenticationRepository {

    override val currentUser: Flow<Either<ErrorBO, UserBO?>> = authenticationRemoteDataSource.currentUser.flowOn(
        Dispatchers.IO
    )

    override suspend fun signIn(loginType: LoginTypeBO): ErrorBO? = withContext(Dispatchers.IO) {
        when (loginType) {
            is LoginTypeBO.Default -> authenticationRemoteDataSource.signIn(loginType.email, loginType.password)
            is LoginTypeBO.Google -> authenticationRemoteDataSource.googleSignIn(loginType.googleAccessToken)
                .saveUserData(GOOGLE_LOGIN_TYPE)
        }
    }

    override suspend fun signUp(email: String, password: String, name: String): ErrorBO? =
        withContext(Dispatchers.IO) {
            authenticationRemoteDataSource.signUp(email, password, name).saveUserData(DEFAULT_LOGIN_TYPE)
        }

    override suspend fun signOut() = withContext(Dispatchers.IO) {
        authenticationRemoteDataSource.signOut()
    }

    override suspend fun forgotPassword(email: String): ErrorBO? = withContext(Dispatchers.IO) {
        authenticationRemoteDataSource.forgotPassword(email)
    }

    private suspend fun Either<ErrorBO, AuthResult>.saveUserData(loginType: String): ErrorBO? = fold(
        ifLeft = { it },
        ifRight = { authResult ->
            authenticationRemoteDataSource.saveUserData(
                uuid = authResult.user?.uid.orEmpty(),
                email = authResult.user?.email.orEmpty(),
                name = authResult.user?.displayName.orEmpty(),
                loginType = loginType
            )
        }
    )

    companion object {
        private const val DEFAULT_LOGIN_TYPE = "default"
        private const val GOOGLE_LOGIN_TYPE = "google"
    }
}