package com.germandebustamante.ringtonemanager.domain.ringtone.usecase

import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneListRepository
import kotlinx.coroutines.flow.Flow

interface GetPopularRingtonesUseCase {
    suspend operator fun invoke(): Flow<List<RingtoneBO>>
}

class GetPopularRingtonesUseCaseImpl(
    private val ringtoneItemRepository: RingtoneListRepository,
) : GetPopularRingtonesUseCase {
    override suspend fun invoke(): Flow<List<RingtoneBO>> = ringtoneItemRepository.popularRingtones
}