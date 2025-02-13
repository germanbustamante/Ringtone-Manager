package com.germandebustamante.ringtonemanager.data.repository

import com.germandebustamante.ringtonemanager.data.datasource.AuthenticationRemoteDataSource
import com.germandebustamante.ringtonemanager.domain.authorization.model.UserBO
import com.germandebustamante.ringtonemanager.domain.authorization.repository.AuthenticationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AuthenticationRepositoryImpl(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
): AuthenticationRepository {
    override val currentUser: Flow<UserBO?> = authenticationRemoteDataSource.currentUser.flowOn(
        Dispatchers.IO
    )

    override suspend fun signIn(email: String, password: String) {
        withContext(Dispatchers.IO) {
            authenticationRemoteDataSource.signIn(email, password)
        }
    }

    override suspend fun signUp(email: String, password: String) {
        withContext(Dispatchers.IO) {
            authenticationRemoteDataSource.signUp(email, password)
        }
    }

    override suspend fun signOut() {
        withContext(Dispatchers.IO) {
            authenticationRemoteDataSource.signOut()
        }
    }
}