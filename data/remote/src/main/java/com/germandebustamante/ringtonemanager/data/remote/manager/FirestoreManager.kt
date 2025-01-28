package com.germandebustamante.ringtonemanager.data.remote.manager

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.tasks.await

object FirestoreManager {

    suspend inline fun <reified T, R> getDocuments(
        action: () -> Task<QuerySnapshot>,
        mapper: (T) -> R,
    ): Either<CustomError, List<R>> = try {
        val result = action().await()
        val list = result.toObjects(T::class.java).map(mapper)
        list.right()
    } catch (exception: Exception) {
        exception.toError().left()
    }

    suspend inline fun <reified T, R> getDocument(
        action: () -> Task<QuerySnapshot>,
        mapper: (T) -> R,
    ): Either<CustomError, R> = try {
        val result = action().await()
        result.documents.firstOrNull()?.toObject(T::class.java)?.let(mapper)?.right()
            ?: CustomError.NotFound.left()
    } catch (exception: Exception) {
        exception.toError().left()
    }

    fun Throwable.toError(): CustomError = when (this) {
        is StorageException -> CustomError.Server(errorCode, message)
        is RuntimeException -> CustomError.ParcelizeException
        else -> CustomError.Unknown(message)
    }

}
