package com.germandebustamante.ringtonemanager.data.datasource

import com.germandebustamante.ringtonemanager.domain.authorization.model.UserBO
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import kotlinx.coroutines.flow.Flow

interface AuthenticationRemoteDataSource {
    val currentUser: Flow<UserBO?>

    suspend fun signIn(email: String, password: String): CustomError?
    suspend fun signUp(email: String, password: String): CustomError?
    fun signOut()
}