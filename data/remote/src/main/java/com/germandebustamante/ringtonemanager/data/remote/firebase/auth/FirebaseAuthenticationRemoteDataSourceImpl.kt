package com.germandebustamante.ringtonemanager.data.remote.firebase.auth

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.authorization.UserBO
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.data.datasource.AuthenticationRemoteDataSource
import com.germandebustamante.ringtonemanager.data.remote.manager.FirebaseAuthManager
import com.germandebustamante.ringtonemanager.data.remote.manager.FirestoreManager
import com.germandebustamante.ringtonemanager.data.remote.manager.toErrorBO
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
    private val authManager: FirebaseAuthManager,
    private val firestoreManager: FirestoreManager,
) : AuthenticationRemoteDataSource {

    override val currentUser: Flow<Either<ErrorBO, UserBO?>> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.let { UserBO(it.uid, it.email.orEmpty()).right() } ?: run { null.right() })
        }
        try {
            firebaseAuth.addAuthStateListener(listener)
        } catch (exception: Exception) {
            trySend(exception.toErrorBO().left())
        }
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override suspend fun signIn(email: String, password: String): Either<ErrorBO, Unit> =
        authManager.execute { firebaseAuth.signInWithEmailAndPassword(email, password) }.map {}

    override suspend fun googleSignIn(googleTokenId: String): Either<ErrorBO, Unit> =
        authManager.execute {
            firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(googleTokenId, null))
        }.flatMap { authResult ->
            saveUserData(authResult, GOOGLE_LOGIN_TYPE)
        }

    override suspend fun signUp(email: String, password: String, name: String): Either<ErrorBO, Unit> = try {
        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(name).build()
        authResult.user?.updateProfile(profileUpdates)?.await()
        saveUserData(authResult, DEFAULT_LOGIN_TYPE)
    } catch (exception: Exception) {
        exception.toErrorBO().left()
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun forgotPassword(email: String): Either<ErrorBO, Unit> {
        val error = authManager.executeVoid { firebaseAuth.sendPasswordResetEmail(email) }
        return if (error != null) error.left() else Unit.right()
    }

    private suspend fun saveUserData(authResult: AuthResult, loginType: String): Either<ErrorBO, Unit> {
        val userInfoMap = hashMapOf(
            USERS_COLLECTION_EMAIL_FIELD to authResult.user?.email.orEmpty(),
            USERS_COLLECTION_NAME_FIELD to authResult.user?.displayName.orEmpty(),
            USERS_COLLECTION_LOGIN_TYPE_FIELD to loginType
        )
        val error = firestoreManager.createDocument(
            firestore.collection(USERS_COLLECTION_NAME)
                .document(authResult.user?.uid.orEmpty())
                .set(userInfoMap, SetOptions.merge())
        )
        return if (error != null) error.left() else Unit.right()
    }

    companion object {
        private const val USERS_COLLECTION_NAME = "users_v1"
        private const val USERS_COLLECTION_EMAIL_FIELD = "email"
        private const val USERS_COLLECTION_NAME_FIELD = "name"
        private const val USERS_COLLECTION_LOGIN_TYPE_FIELD = "loginType"
        private const val DEFAULT_LOGIN_TYPE = "default"
        private const val GOOGLE_LOGIN_TYPE = "google"
    }
}
