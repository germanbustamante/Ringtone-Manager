package com.germandebustamante.ringtonemanager.data.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.data.datasource.AuthenticationRemoteDataSource
import com.germandebustamante.ringtonemanager.domain.authorization.model.UserBO
import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AuthenticationRepositoryImpl(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
) : AuthenticationRepository {
    override val currentUser: Flow<Either<CustomError, UserBO?>> = authenticationRemoteDataSource.currentUser.flowOn(
        Dispatchers.IO
    )

    override suspend fun signIn(email: String, password: String): CustomError? = withContext(Dispatchers.IO) {
        authenticationRemoteDataSource.signIn(email, password)
    }

    override suspend fun signUp(email: String, password: String): CustomError? = withContext(Dispatchers.IO) {
        authenticationRemoteDataSource.signUp(email, password)
    }

    override suspend fun signOut() {
        withContext(Dispatchers.IO) {
            authenticationRemoteDataSource.signOut()
        }
    }
}