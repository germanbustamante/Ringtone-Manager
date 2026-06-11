package com.germandebustamante.ringtonemanager.data.remote.firebase.firestore

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneListRemoteDataSource
import com.germandebustamante.ringtonemanager.data.remote.manager.toErrorBO
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.RingtoneDTO
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.toDomain
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class RingtoneListFirestoreRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore,
) : RingtoneListRemoteDataSource {

    override suspend fun fetchPopularRingtones(): Either<ErrorBO, List<RingtoneBO>> =
        try {
            val snapshot = firestore.collection(COLLECTION_NAME)
                .orderBy(POPULARITY_FIELD, Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.toObjects(RingtoneDTO::class.java).map { it.toDomain() }.right()
        } catch (exception: Exception) {
            exception.toErrorBO().left()
        }

    companion object {
        private const val COLLECTION_NAME = "ringtones_v1"
        private const val POPULARITY_FIELD = "popularity"
    }
}
