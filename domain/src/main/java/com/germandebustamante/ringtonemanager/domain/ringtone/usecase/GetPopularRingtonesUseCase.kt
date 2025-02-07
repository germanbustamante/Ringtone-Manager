package com.germandebustamante.ringtonemanager.domain.ringtone.usecase

import arrow.core.Either
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneListRepository
import kotlinx.coroutines.flow.Flow

interface GetPopularRingtonesUseCase {
    operator fun invoke(): Flow<Either<CustomError, List<RingtoneBO>>>
}

class GetPopularRingtonesUseCaseImpl(
    private val ringtoneItemRepository: RingtoneListRepository,
) : GetPopularRingtonesUseCase {
    override fun invoke(): Flow<Either<CustomError, List<RingtoneBO>>> = ringtoneItemRepository.popularRingtones
}