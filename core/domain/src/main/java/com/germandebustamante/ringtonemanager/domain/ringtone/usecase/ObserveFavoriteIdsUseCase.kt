package com.germandebustamante.ringtonemanager.domain.ringtone.usecase

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow

class ObserveFavoriteIdsUseCase(private val favoritesRepository: FavoritesRepository) {
    operator fun invoke(userId: String): Flow<Either<ErrorBO, Set<String>>> =
        favoritesRepository.observeFavoriteIds(userId)
}
