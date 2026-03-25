package com.germandebustamante.ringtonemanager.data.remote.firebase.firestore

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneListRemoteDataSource
import com.germandebustamante.ringtonemanager.data.remote.manager.FirestoreManager
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.RingtoneDTO
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.toDomain
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow

class RingtoneListFirestoreRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore,
    private val firestoreManager: FirestoreManager,
) : RingtoneListRemoteDataSource {

    /**
     * Implementacion antigua (se mantiene por si se necesita revisar)
     * ```
     * override fun getPopularRingtones(): Flow<Either<CustomError, List<RingtoneBO>>> =
     *         callbackFlow {
     *             firestore.collection(COLLECTION_NAME).addSnapshotListener { value, error ->
     *                 error?.let {
     *                     trySend(it.toError().left())
     *                 }
     *
     *                 value?.let {
     *                     val data = it.toObjects<RingtoneDTO>().map { it.toDomain() }
     *                     trySend(data.right())
     *                 }
     *             }
     *             awaitClose { close() }
     *         }
     * ```
     */
    override fun getPopularRingtones(): Flow<Either<ErrorBO, List<RingtoneBO>>> =
        firestoreManager.getDocumentsFlow<RingtoneDTO, RingtoneBO>(
            action = { firestore.collection(COLLECTION_NAME).orderBy(POPULARITY_FIELD, Query.Direction.DESCENDING) },
            mapper = RingtoneDTO::toDomain,
        )

    companion object {
        private const val COLLECTION_NAME = "ringtones_v1"
        private const val POPULARITY_FIELD = "popularity"
    }
}
