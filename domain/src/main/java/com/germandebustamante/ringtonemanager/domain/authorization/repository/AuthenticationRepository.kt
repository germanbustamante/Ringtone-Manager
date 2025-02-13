package com.germandebustamante.ringtonemanager.domain.authorization.repository

import com.germandebustamante.ringtonemanager.domain.authorization.model.UserBO
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    val currentUser: Flow<UserBO?>

    suspend fun signIn(email: String, password: String)
    suspend fun signUp(email: String, password: String)
    suspend fun signOut()
}