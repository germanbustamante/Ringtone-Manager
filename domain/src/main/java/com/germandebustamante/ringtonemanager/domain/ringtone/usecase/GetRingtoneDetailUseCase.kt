package com.germandebustamante.ringtonemanager.domain.ringtone.usecase

import arrow.core.Either
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneRepository

interface GetRingtoneDetailUseCase {
    suspend operator fun invoke(ringtoneId: String): Either<CustomError, RingtoneBO>
}

class GetRingtoneDetailUseCaseImpl(private val ringtoneRepository: RingtoneRepository) : GetRingtoneDetailUseCase {
    override suspend fun invoke(ringtoneId: String): Either<CustomError, RingtoneBO> = ringtoneRepository.getRingtoneDetail(ringtoneId)
}