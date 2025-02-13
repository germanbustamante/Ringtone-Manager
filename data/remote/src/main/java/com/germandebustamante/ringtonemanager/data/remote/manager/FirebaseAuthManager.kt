package com.germandebustamante.ringtonemanager.data.remote.manager

import com.germandebustamante.ringtonemanager.data.remote.manager.FirestoreManager.toError
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await

object FirebaseAuthManager {

    suspend fun execute(action: () -> Task<AuthResult>): CustomError? =
        try {
            action().await()
            null
        } catch (exception: Exception) {
            exception.toError()
        }
}