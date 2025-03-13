package com.germandebustamante.ringtonemanager.data.remote.manager

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.domain.authorization.model.UserBO
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

object FirestoreManager {


    /**
     * Obtiene un flujo de documentos desde una fuente de datos utilizando un mapeador para transformar los datos.
     *
     * Este método es una función de orden superior que utiliza coroutines para realizar operaciones asíncronas
     * sobre una colección de documentos. Permite definir una consulta para obtener los documentos y un mapeador
     * que transforma cada documento en el tipo de datos deseado.
     *
     * @param T El tipo de dato de los documentos que se obtienen de la consulta.
     * @param R El tipo de dato al que se transforman los documentos después de aplicar el mapeador.
     * @param action Una función que devuelve una consulta (`Query`) para obtener los documentos.
     * @param mapper Una función que transforma un documento de tipo `T` a `R`.
     * @return Un flujo (`Flow`) que emite un `Either` con un `CustomError` en caso de error, o una lista con los
     *         documentos transformados de tipo `R`.
     *
     * Ejemplo de uso:
     * ```
     * override fun getPopularRingtones(): Flow<Either<CustomError, List<RingtoneBO>>> =
     *     FirestoreManager.getDocumentsFlow<RingtoneDTO, RingtoneBO>(
     *         action = { firestore.collection(COLLECTION_NAME) },
     *         mapper = RingtoneDTO::toDomain,
     *     )
     * ```
     */
    inline fun <reified T : Any, R> getDocumentsFlow(
        action: () -> Query,
        crossinline mapper: (T) -> R,
    ): Flow<Either<CustomError, List<R>>> = action().dataObjects<T>().map {
        it.map(mapper).right()
    }.catch {
        it.toError().left()
    }

    suspend inline fun <reified T, R> getDocument(
        action: () -> Task<DocumentSnapshot>,
        mapper: (T) -> R,
    ): Either<CustomError, R> = try {
        val result = action().await()
        result.toObject(T::class.java)?.let(mapper)?.right()
            ?: CustomError.NotFound.left()
    } catch (exception: Exception) {
        exception.toError().left()
    }

    suspend inline fun createDocument(action: Task<Void>): CustomError? =  try {
        action.await()
        null
    } catch (exception: Exception) {
        exception.toError()
    }


    fun Throwable.toError(): CustomError = when (this) {
        is StorageException -> CustomError.Server(errorCode, message)
        is RuntimeException -> CustomError.ParcelizeException
        is FirebaseAuthUserCollisionException -> CustomError.EmailAddressAlreadyInUse //Invoked when we call register with a email that is already in use
        is FirebaseAuthInvalidCredentialsException -> CustomError.InvalidCredentials   //When try to login with a non existent email AND if try login with bad password but user exists
        else -> CustomError.Unknown(message)
    }

}
