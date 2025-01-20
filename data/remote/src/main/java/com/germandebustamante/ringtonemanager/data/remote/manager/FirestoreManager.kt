package com.germandebustamante.ringtonemanager.data.remote.manager

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.tasks.await

object FirestoreManager {

    suspend inline fun <reified T, R> getDocuments(
        action: () -> CollectionReference,
        mapperToDomainLayer: (T) -> R,
    ): Either<CustomError, List<R>> {
        try {
            val success = action().get().await()
            val list = success.toObjects(T::class.java).toList()
            return list.map(mapperToDomainLayer).right()
        } catch (exception: Exception) {
            return exception.toError().left()
        }
    }

    fun Throwable.toError(): CustomError = when (this) {
        is StorageException -> CustomError.Server(errorCode, message)
        is RuntimeException -> CustomError.ParcelizeException
        else -> CustomError.Unknown(message)
    }

}
