package com.germandebustamante.ringtonemanager.data.datasource

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import kotlinx.coroutines.flow.Flow

interface FavoritesRemoteDataSource {
    fun observeFavoriteIds(userId: String): Flow<Either<ErrorBO, Set<String>>>
    suspend fun addFavorite(userId: String, ringtoneId: String): Either<ErrorBO, Unit>
    suspend fun removeFavorite(userId: String, ringtoneId: String): Either<ErrorBO, Unit>
}
