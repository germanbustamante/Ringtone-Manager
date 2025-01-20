package com.germandebustamante.ringtonemanager.domain.ringtone.usecase

import arrow.core.Either
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneRepository

interface GetFullRingtonesUseCase {
    suspend operator fun invoke(): Either<CustomError, List<RingtoneBO>>
}

class GetFullRingtonesUseCaseImpl(private val ringtoneRepository: RingtoneRepository) : GetFullRingtonesUseCase {
    override suspend fun invoke(): Either<CustomError, List<RingtoneBO>> = ringtoneRepository.getFullRingtones()
}