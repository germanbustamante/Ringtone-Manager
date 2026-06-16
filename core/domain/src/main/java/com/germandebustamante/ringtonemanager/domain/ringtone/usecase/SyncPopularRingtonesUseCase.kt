package com.germandebustamante.ringtonemanager.domain.ringtone.usecase

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneListRepository

class SyncPopularRingtonesUseCase(
    private val ringtoneListRepository: RingtoneListRepository,
) {
    suspend operator fun invoke(): Either<ErrorBO, Unit> = ringtoneListRepository.syncPopularRingtones()
}
