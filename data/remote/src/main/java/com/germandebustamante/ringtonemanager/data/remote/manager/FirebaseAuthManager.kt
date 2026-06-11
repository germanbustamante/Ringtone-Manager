package com.germandebustamante.ringtonemanager.data.remote.manager

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await

class FirebaseAuthManager {

    suspend fun execute(action: () -> Task<AuthResult>): Either<ErrorBO, AuthResult> =
        try {
            action().await().right()
        } catch (exception: Exception) {
            exception.toErrorBO().left()
        }

    suspend fun executeVoid(action: () -> Task<Void>): Either<ErrorBO, Unit> =
        try {
            action().await()
            Unit.right()
        } catch (exception: Exception) {
            exception.toErrorBO().left()
        }
}
