package com.germandebustamante.ringtonemanager.data.remote.manager

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreManager {

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
     * @return Un flujo (`Flow`) que emite un `Either` con un `ErrorBO` en caso de error, o una lista con los
     *         documentos transformados de tipo `R`.
     */
    inline fun <reified T : Any, R> getDocumentsFlow(
        action: () -> Query,
        crossinline mapper: (T) -> R,
    ): Flow<Either<ErrorBO, List<R>>> = action().dataObjects<T>().map { list ->
        list.map(mapper).right<List<R>>() as Either<ErrorBO, List<R>>
    }.catch { throwable ->
        emit(Either.Left(throwable.toErrorBO()))
    }

    suspend inline fun <reified T, R> getDocument(
        action: () -> Task<DocumentSnapshot>,
        mapper: (T) -> R,
    ): Either<ErrorBO, R> = try {
        val result = action().await()
        result.toObject(T::class.java)?.let(mapper)?.right()
            ?: ErrorBO.NotFound.left()
    } catch (exception: Exception) {
        exception.toErrorBO().left()
    }

    suspend fun createDocument(action: Task<Void>): ErrorBO? = try {
        action.await()
        null
    } catch (exception: Exception) {
        exception.toErrorBO()
    }
}
