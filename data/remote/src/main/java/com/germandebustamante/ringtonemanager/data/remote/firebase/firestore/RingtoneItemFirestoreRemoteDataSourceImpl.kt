package com.germandebustamante.ringtonemanager.data.remote.firebase.firestore

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneItemRemoteDataSource
import com.germandebustamante.ringtonemanager.data.remote.manager.FirestoreManager
import com.germandebustamante.ringtonemanager.data.remote.manager.toErrorBO
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.RingtoneDTO
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.toDomain
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RingtoneItemFirestoreRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore,
    private val firestoreManager: FirestoreManager,
) : RingtoneItemRemoteDataSource {

    override suspend fun getRingtoneDetail(ringtoneId: String): Either<ErrorBO, RingtoneBO> =
        firestoreManager.getDocument<RingtoneDTO, RingtoneBO>(
            action = { firestore.collection(COLLECTION_NAME).document(ringtoneId).get() },
            mapper = { it.toDomain() },
        )

    override suspend fun incrementPopularity(ringtoneId: String): Either<ErrorBO, Unit> =
        try {
            firestore.collection(COLLECTION_NAME)
                .document(ringtoneId)
                .update(FIELD_POPULARITY, FieldValue.increment(POPULARITY_INCREMENT))
                .await()
            Unit.right()
        } catch (exception: Exception) {
            exception.toErrorBO().left()
        }

    companion object {
        private const val COLLECTION_NAME = "ringtones_v1"
        private const val FIELD_POPULARITY = "popularity"
        private const val POPULARITY_INCREMENT = 1L
    }
}
