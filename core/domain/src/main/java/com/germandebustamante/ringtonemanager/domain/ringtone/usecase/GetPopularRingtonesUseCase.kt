package com.germandebustamante.ringtonemanager.domain.ringtone.usecase

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneListRepository
import kotlinx.coroutines.flow.Flow

class GetPopularRingtonesUseCase(
    private val ringtoneItemRepository: RingtoneListRepository,
) {
    operator fun invoke(): Flow<Either<ErrorBO, List<RingtoneBO>>> = ringtoneItemRepository.popularRingtones
}