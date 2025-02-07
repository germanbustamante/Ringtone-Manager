package com.germandebustamante.ringtonemanager.data.remote.firestore

import arrow.core.Either
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneListRemoteDataSource
import com.germandebustamante.ringtonemanager.data.remote.manager.FirestoreManager
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.RingtoneDTO
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.toDomain
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow

class RingtoneListFirestoreRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore,
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
    override fun getPopularRingtones(): Flow<Either<CustomError, List<RingtoneBO>>> =
        FirestoreManager.getDocumentsFlow<RingtoneDTO, RingtoneBO>(
            action = { firestore.collection(COLLECTION_NAME) },
            mapper = RingtoneDTO::toDomain,
        )

    companion object {
        private const val COLLECTION_NAME = "ringtones_v1"
    }
}