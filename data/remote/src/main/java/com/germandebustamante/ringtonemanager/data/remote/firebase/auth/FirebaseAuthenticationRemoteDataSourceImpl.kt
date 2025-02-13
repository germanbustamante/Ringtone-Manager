package com.germandebustamante.ringtonemanager.data.remote.firebase.auth

import com.germandebustamante.ringtonemanager.data.datasource.AuthenticationRemoteDataSource
import com.germandebustamante.ringtonemanager.data.remote.manager.FirebaseAuthManager
import com.germandebustamante.ringtonemanager.domain.authorization.model.UserBO
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseAuthenticationRemoteDataSourceImpl(
    private val firebaseAuth: FirebaseAuth,
) : AuthenticationRemoteDataSource {

    override val currentUser: Flow<UserBO?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.let { UserBO(it.uid) })
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override suspend fun signIn(email: String, password: String): CustomError? = FirebaseAuthManager.execute {
        firebaseAuth.signInWithEmailAndPassword(email, password)
    }

    override suspend fun signUp(email: String, password: String): CustomError? = FirebaseAuthManager.execute {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}