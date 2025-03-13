package com.germandebustamante.ringtonemanager.data.remote.firebase.auth

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.data.datasource.AuthenticationRemoteDataSource
import com.germandebustamante.ringtonemanager.data.remote.manager.FirebaseAuthManager
import com.germandebustamante.ringtonemanager.data.remote.manager.FirestoreManager
import com.germandebustamante.ringtonemanager.data.remote.manager.FirestoreManager.toError
import com.germandebustamante.ringtonemanager.domain.authorization.model.UserBO
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseAuthenticationRemoteDataSourceImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : AuthenticationRemoteDataSource {

    override val currentUser: Flow<Either<CustomError, UserBO?>> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.let { UserBO(it.uid, it.email.orEmpty()).right() } ?: run { null.right() })
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
    }.swap().getOrNull()

    override suspend fun googleSignIn(googleTokenId: String): Either<CustomError, AuthResult> =
        FirebaseAuthManager.execute {
            firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(googleTokenId, null))
        }

    override suspend fun signUp(email: String, password: String, name: String): Either<CustomError, AuthResult> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            authResult.user?.updateProfile(profileUpdates)?.await()
            authResult.right()
        } catch (exception: Exception) {
            exception.toError().left()
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun forgotPassword(email: String): CustomError? = FirebaseAuthManager.executeVoid {
        firebaseAuth.sendPasswordResetEmail(email)
    }

    override suspend fun saveUserData(uuid: String, email: String, name: String?, loginType: String): CustomError? {
        return try {
            val userInfoMap = hashMapOf(
                USERS_COLLECTION_EMAIL_FIELD to email,
                USERS_COLLECTION_NAME_FIELD to name,
                USERS_COLLECTION_LOGIN_TYPE_FIELD to loginType
            )

            FirestoreManager.createDocument(
                firestore.collection(USERS_COLLECTION_NAME)
                    .document(uuid)
                    .set(userInfoMap, SetOptions.merge())
            )
        } catch (e: Exception) {
            e.toError()
        }
    }

    companion object {
        private const val USERS_COLLECTION_NAME = "users_v1"
        private const val USERS_COLLECTION_EMAIL_FIELD = "email"
        private const val USERS_COLLECTION_NAME_FIELD = "name"
        private const val USERS_COLLECTION_LOGIN_TYPE_FIELD = "loginType"
    }
}