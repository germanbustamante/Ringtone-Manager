package com.germandebustamante.ringtonemanager.data.remote.firebase.firestore

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.data.datasource.FavoritesRemoteDataSource
import com.germandebustamante.ringtonemanager.data.remote.manager.FirestoreManager
import com.germandebustamante.ringtonemanager.data.remote.manager.toErrorBO
import com.germandebustamante.ringtonemanager.data.remote.model.favorite.FavoriteDTO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FavoritesFirestoreRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore,
    private val firestoreManager: FirestoreManager,
) : FavoritesRemoteDataSource {

    override fun observeFavoriteIds(userId: String): Flow<Either<ErrorBO, Set<String>>> =
        firestoreManager.getDocumentsFlow<FavoriteDTO, String>(
            action = { firestore.collection(USERS).document(userId).collection(FAVORITES) },
            mapper = { it.ringtoneId },
        ).map { either -> either.map { it.toSet() } }

    override suspend fun addFavorite(userId: String, ringtoneId: String): Either<ErrorBO, Unit> =
        try {
            firestore.collection(USERS).document(userId)
                .collection(FAVORITES).document(ringtoneId)
                .set(FavoriteDTO(ringtoneId))
                .await()
            Unit.right()
        } catch (e: Exception) {
            e.toErrorBO().left()
        }

    override suspend fun removeFavorite(userId: String, ringtoneId: String): Either<ErrorBO, Unit> =
        try {
            firestore.collection(USERS).document(userId)
                .collection(FAVORITES).document(ringtoneId)
                .delete()
                .await()
            Unit.right()
        } catch (e: Exception) {
            e.toErrorBO().left()
        }

    companion object {
        private const val USERS = "users_v1"
        private const val FAVORITES = "favorites"
    }
}
