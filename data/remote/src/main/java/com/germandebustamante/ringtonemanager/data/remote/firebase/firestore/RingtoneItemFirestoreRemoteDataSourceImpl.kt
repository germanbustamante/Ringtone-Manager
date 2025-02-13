package com.germandebustamante.ringtonemanager.data.remote.firebase.firestore

import arrow.core.Either
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneItemRemoteDataSource
import com.germandebustamante.ringtonemanager.data.remote.manager.FirestoreManager
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.RingtoneDTO
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.toDomain
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.google.firebase.firestore.FirebaseFirestore

class RingtoneItemFirestoreRemoteDataSourceImpl(private val firestore: FirebaseFirestore) : RingtoneItemRemoteDataSource {

    override suspend fun getRingtoneDetail(ringtoneId: String): Either<CustomError, RingtoneBO> =
        FirestoreManager.getDocument<RingtoneDTO, RingtoneBO>(
            action = { firestore.collection(COLLECTION_NAME).document(ringtoneId).get() },
            mapper = { it.toDomain() },
        ).onRight {
            firestore.collection(COLLECTION_NAME).document(ringtoneId).update("popularity", it.popularity + 1)
        }

    companion object {
        private const val COLLECTION_NAME = "ringtones_v1"
    }
}