package com.germandebustamante.ringtonemanager.domain.ringtone.usecase

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneItemRepository

class IncrementRingtonePopularityUseCase(
    private val ringtoneItemRepository: RingtoneItemRepository,
) {
    suspend operator fun invoke(ringtoneId: String): Either<ErrorBO, Unit> =
        ringtoneItemRepository.incrementPopularity(ringtoneId)
}
