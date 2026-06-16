package com.germandebustamante.ringtonemanager.domain.ringtone.usecase

import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneListRepository
import kotlinx.coroutines.flow.Flow

class GetPopularRingtonesUseCase(
    private val ringtoneListRepository: RingtoneListRepository,
) {
    operator fun invoke(): Flow<List<RingtoneBO>> = ringtoneListRepository.popularRingtones
}
