package com.germandebustamante.ringtonemanager.domain.ringtone.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavoriteIds(userId: String): Flow<Either<ErrorBO, Set<String>>>
    suspend fun toggleFavorite(userId: String, ringtoneId: String, isFavorite: Boolean): Either<ErrorBO, Unit>
}
