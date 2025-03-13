package com.germandebustamante.ringtonemanager.data.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.data.datasource.AuthenticationRemoteDataSource
import com.germandebustamante.ringtonemanager.domain.authorization.model.LoginTypeBO
import com.germandebustamante.ringtonemanager.domain.authorization.model.UserBO
import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

//TODO MAYBE THE AUTHENTICATION LOGIC TO SAVE USER DATA SHOULD BE IN OTHER DATASOURCE
class AuthenticationRepositoryImpl(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
) : AuthenticationRepository {

    override val currentUser: Flow<Either<CustomError, UserBO?>> = authenticationRemoteDataSource.currentUser.flowOn(
        Dispatchers.IO
    )

    override suspend fun signIn(loginType: LoginTypeBO): CustomError? = withContext(Dispatchers.IO) {
        when (loginType) {
            is LoginTypeBO.Default -> authenticationRemoteDataSource.signIn(loginType.email, loginType.password)
            is LoginTypeBO.Google -> authenticationRemoteDataSource.googleSignIn(loginType.googleAccessToken)
                .saveUserData(GOOGLE_LOGIN_TYPE)

            else -> CustomError.InvalidCredentials
        }
    }

    override suspend fun signUp(email: String, password: String, name: String): CustomError? =
        withContext(Dispatchers.IO) {
            authenticationRemoteDataSource.signUp(email, password, name).saveUserData(DEFAULT_LOGIN_TYPE)
        }

    override suspend fun signOut() = withContext(Dispatchers.IO) {
        authenticationRemoteDataSource.signOut()
    }

    override suspend fun forgotPassword(email: String): CustomError? = withContext(Dispatchers.IO) {
        authenticationRemoteDataSource.forgotPassword(email)
    }

    private suspend fun Either<CustomError, AuthResult>.saveUserData(loginType: String): CustomError? = fold(
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