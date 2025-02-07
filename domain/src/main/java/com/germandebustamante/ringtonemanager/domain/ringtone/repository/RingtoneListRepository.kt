package com.germandebustamante.ringtonemanager.domain.ringtone.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.domain.error.CustomError
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import kotlinx.coroutines.flow.Flow

interface RingtoneListRepository {
    val popularRingtones: Flow<Either<CustomError, List<RingtoneBO>>>
}