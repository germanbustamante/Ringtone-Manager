package com.germandebustamante.ringtonemanager.domain.ringtone.usecase

import arrow.core.Either
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.AnalyticsRepository
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneItemRepository

interface GetRingtoneDetailUseCase {
    suspend operator fun invoke(ringtoneId: String): Either<CustomError, RingtoneBO>
}

class GetRingtoneDetailUseCaseImpl(
    private val ringtoneItemRepository: RingtoneItemRepository,
    private val analyticsRepository: AnalyticsRepository,
) : GetRingtoneDetailUseCase {
    override suspend fun invoke(ringtoneId: String): Either<CustomError, RingtoneBO> =
        ringtoneItemRepository.getRingtoneDetail(ringtoneId).onRight {
            analyticsRepository.onRingtoneObtained(ringtoneId, it.name)
        }
}