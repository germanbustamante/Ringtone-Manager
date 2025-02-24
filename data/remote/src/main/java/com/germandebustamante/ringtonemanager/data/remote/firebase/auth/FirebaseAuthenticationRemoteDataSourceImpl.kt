package com.germandebustamante.ringtonemanager.data.remote.firebase.auth

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.data.datasource.AuthenticationRemoteDataSource
import com.germandebustamante.ringtonemanager.data.remote.manager.FirebaseAuthManager
import com.germandebustamante.ringtonemanager.data.remote.manager.FirestoreManager.toError
import com.germandebustamante.ringtonemanager.domain.authorization.model.UserBO
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseAuthenticationRemoteDataSourceImpl(
    private val firebaseAuth: FirebaseAuth,
) : AuthenticationRemoteDataSource {

    override val currentUser: Flow<Either<CustomError, UserBO?>> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.let { UserBO(it.uid).right() } ?: run { null.right() })
        }
        try {
            firebaseAuth.addAuthStateListener(listener)
        } catch (exception: Exception) {
            trySend(exception.toError().left())
        }
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override suspend fun signIn(email: String, password: String): CustomError? = FirebaseAuthManager.execute {
        firebaseAuth.signInWithEmailAndPassword(email, password)
    }

    override suspend fun googleSignIn(googleTokenId: String): CustomError? = FirebaseAuthManager.execute {
        firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(googleTokenId, null))
    }

    override suspend fun signUp(email: String, password: String): CustomError? = FirebaseAuthManager.execute {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun forgotPassword(email: String): CustomError? = FirebaseAuthManager.executeVoid {
        firebaseAuth.sendPasswordResetEmail(email)
    }
}