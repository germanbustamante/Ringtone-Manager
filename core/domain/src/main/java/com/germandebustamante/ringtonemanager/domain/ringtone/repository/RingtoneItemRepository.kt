package com.germandebustamante.ringtonemanager.domain.ringtone.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO

interface RingtoneItemRepository {
    suspend fun getRingtoneDetail(ringtoneId: String): Either<ErrorBO, RingtoneBO>

    suspend fun incrementPopularity(ringtoneId: String): Either<ErrorBO, Unit>
}