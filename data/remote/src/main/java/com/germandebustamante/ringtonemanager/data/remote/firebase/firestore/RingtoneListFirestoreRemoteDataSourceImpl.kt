package com.germandebustamante.ringtonemanager.data.remote.firebase.firestore

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneListPage
import com.germandebustamante.ringtonemanager.data.datasource.RingtoneListRemoteDataSource
import com.germandebustamante.ringtonemanager.data.datasource.RingtonePageCursor
import com.germandebustamante.ringtonemanager.data.remote.manager.toErrorBO
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.RingtoneDTO
import com.germandebustamante.ringtonemanager.data.remote.model.ringtone.toDomain
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class RingtoneListFirestoreRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore,
) : RingtoneListRemoteDataSource {

    override suspend fun fetchPopularRingtones(
        pageSize: Int,
        cursor: RingtonePageCursor?,
    ): Either<ErrorBO, RingtoneListPage> =
        try {
            var query = firestore.collection(COLLECTION_NAME)
                .orderBy(POPULARITY_FIELD, Query.Direction.DESCENDING)
                .orderBy(FieldPath.documentId(), Query.Direction.DESCENDING)
                .limit(pageSize.toLong())

            if (cursor != null) {
                query = query.startAfter(cursor.popularity, cursor.documentId)
            }

            val snapshot = query.get().await()
            val ringtones = snapshot.toObjects(RingtoneDTO::class.java).map { it.toDomain() }
            val lastDoc = snapshot.documents.lastOrNull()
            val hasMore = snapshot.size() >= pageSize

            val nextCursor = if (lastDoc != null && hasMore) {
                val popularity = lastDoc.getLong(POPULARITY_FIELD)?.toInt() ?: 0
                RingtonePageCursor(popularity, lastDoc.id)
            } else {
                null
            }

            RingtoneListPage(ringtones, nextCursor, hasMore).right()
        } catch (exception: Exception) {
            exception.toErrorBO().left()
        }

    companion object {
        private const val COLLECTION_NAME = "ringtones_v1"
        private const val POPULARITY_FIELD = "popularity"
    }
}
