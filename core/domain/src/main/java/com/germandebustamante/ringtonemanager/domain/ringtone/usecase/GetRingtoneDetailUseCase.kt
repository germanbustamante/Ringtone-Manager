package com.germandebustamante.ringtonemanager.domain.ringtone.usecase

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.AnalyticsRepository
import com.germandebustamante.ringtonemanager.domain.ringtone.repository.RingtoneItemRepository

class GetRingtoneDetailUseCase(
    private val ringtoneItemRepository: RingtoneItemRepository,
    private val analyticsRepository: AnalyticsRepository,
) {
    suspend operator fun invoke(ringtoneId: String): Either<ErrorBO, RingtoneBO> =
        ringtoneItemRepository.getRingtoneDetail(ringtoneId).onRight {
            analyticsRepository.onRingtoneObtained(ringtoneId, it.name)
        }
}