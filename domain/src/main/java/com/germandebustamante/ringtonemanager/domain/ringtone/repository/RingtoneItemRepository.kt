package com.germandebustamante.ringtonemanager.domain.ringtone.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO

interface RingtoneItemRepository {
    suspend fun getRingtoneDetail(ringtoneId: String): Either<CustomError, RingtoneBO>
}