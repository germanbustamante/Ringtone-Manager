package com.germandebustamante.ringtonemanager.data.remote.firebase.firestore

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneItemRemoteDataSource
import com.germandebustamante.ringtonemanager.data.remote.manager.FirestoreManager
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.RingtoneDTO
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.toDomain
import com.google.firebase.firestore.FirebaseFirestore

class RingtoneItemFirestoreRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore,
    private val firestoreManager: FirestoreManager,
) : RingtoneItemRemoteDataSource {

    override suspend fun getRingtoneDetail(ringtoneId: String): Either<ErrorBO, RingtoneBO> =
        firestoreManager.getDocument<RingtoneDTO, RingtoneBO>(
            action = { firestore.collection(COLLECTION_NAME).document(ringtoneId).get() },
            mapper = { it.toDomain() },
        ).onRight {
            firestore.collection(COLLECTION_NAME)
                .document(ringtoneId).update("popularity", it.popularity + 1)
        }

    companion object {
        private const val COLLECTION_NAME = "ringtones_v1"
    }
}
