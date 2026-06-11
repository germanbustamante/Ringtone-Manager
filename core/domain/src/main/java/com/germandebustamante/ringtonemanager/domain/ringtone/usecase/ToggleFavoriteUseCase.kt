package com.germandebustamante.ringtonemanager.domain.ringtone.usecase

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.FavoritesRepository

class ToggleFavoriteUseCase(private val favoritesRepository: FavoritesRepository) {
    suspend operator fun invoke(
        userId: String,
        ringtoneId: String,
        isFavorite: Boolean,
    ): Either<ErrorBO, Unit> = favoritesRepository.toggleFavorite(userId, ringtoneId, isFavorite)
}
