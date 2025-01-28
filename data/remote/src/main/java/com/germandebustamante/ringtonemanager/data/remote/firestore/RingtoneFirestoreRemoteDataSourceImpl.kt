package com.germandebustamante.ringtonemanager.data.remote.firestore

import arrow.core.Either
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneRemoteDataSource
import com.germandebustamante.ringtonemanager.data.remote.manager.FirestoreManager
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.RingtoneDTO
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.toDomain
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.google.firebase.firestore.FirebaseFirestore

class RingtoneFirestoreRemoteDataSourceImpl(private val firestore: FirebaseFirestore) : RingtoneRemoteDataSource {
    override suspend fun getFullRingtones(): Either<CustomError, List<RingtoneBO>> {
        return FirestoreManager.getDocuments<RingtoneDTO, RingtoneBO>(
            action = { firestore.collection(COLLECTION_NAME).get() },
            mapper = { it.toDomain() },
        )
    }

    override suspend fun getRingtoneDetail(ringtoneId: String): Either<CustomError, RingtoneBO> =
        FirestoreManager.getDocument<RingtoneDTO, RingtoneBO>(
            action = { firestore.collection(COLLECTION_NAME).whereEqualTo(FIELD_ID, ringtoneId.toIntOrNull()).get() },
            mapper = { it.toDomain() },
        )

    companion object {
        private const val COLLECTION_NAME = "ringtones_v1"
        private const val FIELD_ID = "id"
    }
}