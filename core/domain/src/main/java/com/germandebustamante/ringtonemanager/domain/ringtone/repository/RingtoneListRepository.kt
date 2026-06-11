package com.germandebustamante.ringtonemanager.domain.ringtone.repository

import arrow.core.Either
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import kotlinx.coroutines.flow.Flow

interface RingtoneListRepository {
    val popularRingtones: Flow<List<RingtoneBO>>
    suspend fun syncPopularRingtones(): Either<ErrorBO, Unit>
}
